package com.movsoftware.blockhouse.route_tracker.controller;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.movsoftware.blockhouse.route_tracker.entities.Gym;
import com.movsoftware.blockhouse.route_tracker.entities.Log;
import com.movsoftware.blockhouse.route_tracker.entities.Route;
import com.movsoftware.blockhouse.route_tracker.entities.User;
import com.movsoftware.blockhouse.route_tracker.entities.Wall;
import com.movsoftware.blockhouse.route_tracker.pojo.Grade;
import com.movsoftware.blockhouse.route_tracker.pojo.SortBy;
import com.movsoftware.blockhouse.route_tracker.security.RequiresPermission;
import com.movsoftware.blockhouse.route_tracker.service.EntityService;
import com.movsoftware.blockhouse.route_tracker.service.GymService;
import com.movsoftware.blockhouse.route_tracker.service.JwtService;
import com.movsoftware.blockhouse.route_tracker.service.LogService;
import com.movsoftware.blockhouse.route_tracker.service.RouteService;
import com.movsoftware.blockhouse.route_tracker.service.UserService;
import com.movsoftware.blockhouse.route_tracker.service.WallService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/nav")
public class NavController {

    @Value("${main.server.url}")
    private String mainServerUrl;

    private final GymService gymService;
    private final EntityService entityService;
    private final WallService wallService;
    private final RouteService routeService;
    private final LogService logService;
    private final UserService userService;
    private final JwtService jwtService;

    // @Autowired
    public NavController(GymService gymService, LogService logService,
            UserService userService, EntityService entityService, WallService wallService, RouteService routeService,
            JwtService jwtService) {
        this.gymService = gymService;
        this.logService = logService;
        this.userService = userService;
        this.entityService = entityService;
        this.wallService = wallService;
        this.routeService = routeService;
        this.jwtService = jwtService;
    }

    @GetMapping("/ping")
    public String ping() {
        return "Pong!";
    }

    @GetMapping("/get_route_explorer/{gymId}")
    @RequiresPermission({ "VIEWER" })
    public ModelAndView getRouteExplorer(@PathVariable String gymId, Model model,
            HttpSession session) {

        User loggedInUser = entityService.findLoggedInUserOrFail(gymId, session);
        Optional<Gym> gym = gymService.findById(gymId);

        String jwt = (String) session.getAttribute("jwt");
        List<String> permissions = jwtService.getPermissionsFromJwt(jwt);
        session.setAttribute("user_permissions", permissions);

        if (!gym.isPresent()) {
            return new ModelAndView("error");
        }

        // Get sort by from user
        SortBy sortBy = loggedInUser.getSortBy();

        // Fetch active walls
        List<Wall> activeWalls = wallService.findAllActiveWalls(gymId);

        // Map to store whether a user has climbed a specific route
        Map<String, Boolean> hasClimbedMap = new HashMap<>();

        for (Log previousLog : loggedInUser.getLogbook()) {
            hasClimbedMap.put(previousLog.getRoute().getId(), true);
        }

        // Fetch routes associated with each active wall
        Map<String, List<Route>> wallIdToRoutesMap = new HashMap<>();
        for (Wall wall : activeWalls) {
            List<Route> routesForWall = routeService.findAllByWallId(wall.getId());

            // Update hasClimbedMap for the new routes
            for (Route route : routesForWall) {
                hasClimbedMap.putIfAbsent(route.getId(), false);
            }

            if (sortBy == SortBy.HARDEST) {
                routesForWall.sort((r1, r2) -> {
                    Grade grade1 = Grade.valueOf(r1.getGrade());
                    Grade grade2 = Grade.valueOf(r2.getGrade());
                    return Integer.compare(grade2.getBasePoints(), grade1.getBasePoints());
                });
            } else if (sortBy == SortBy.EASIEST) {
                routesForWall.sort((r1, r2) -> {
                    Grade grade1 = Grade.valueOf(r1.getGrade());
                    Grade grade2 = Grade.valueOf(r2.getGrade());
                    return Integer.compare(grade1.getBasePoints(), grade2.getBasePoints());
                });
            } else if (sortBy == SortBy.RATING) {
                routesForWall.sort(Comparator.comparingDouble(Route::getRating).reversed());
            } else if (sortBy == SortBy.MOST_REPEATED) {
                routesForWall.sort(Comparator.comparingInt(Route::getTotalSends).reversed());
            } else if (sortBy == SortBy.LEAST_REPEATED) {
                routesForWall.sort(Comparator.comparingInt(Route::getTotalSends));
            }
            wallIdToRoutesMap.put(wall.getId(), routesForWall);
        }

        String updateMessage = (String) model.asMap().get("updateMessage");

        if (permissions.contains("ADMIN")) {
            model.addAttribute("is_admin", true);
        } else {
            model.addAttribute("is_admin", false);
        }

        model.addAttribute("updateMessage", updateMessage);
        model.addAttribute("activeWalls", activeWalls);
        model.addAttribute("wallIdToRoutesMap", wallIdToRoutesMap);
        model.addAttribute("gym", gym.get());
        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("hasClimbedMap", hasClimbedMap);
        return new ModelAndView("routeExplorer");
    }

    @GetMapping("/get_logbook/{gymId}")
    @RequiresPermission({ "VIEWER" })
    public ModelAndView getLogbook(@PathVariable String gymId, HttpSession session, Model model) {
        List<String> permissions = (List<String>) session.getAttribute("user_permissions");
        System.out.println(permissions);
        Optional<Gym> gym = gymService.findById(gymId);

        if (!gym.isPresent()) {
            return new ModelAndView("error");
        }

        User loggedInUser = entityService.findLoggedInUserOrFail(gymId, session);

        List<Log> logs = logService.getLogsByUserId(loggedInUser.getUid());
        List<Route> routes = new ArrayList<>(); // Initialize the list

        // Initialize logsGroupedByDate
        Map<String, List<Log>> logsGroupedByDate = new TreeMap<>(Collections.reverseOrder()); // Sorting by date in
                                                                                              // descending order

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-YY"); // Change this to your preferred date
                                                                               // format

        for (Log log : logs) {
            String formattedDate = log.getSentDate().toLocalDate().format(formatter);
            logsGroupedByDate
                    .computeIfAbsent(formattedDate, k -> new ArrayList<>())
                    .add(log);

            routes.add(log.getRoute());
        }

        if (permissions.contains("ADMIN")) {
            model.addAttribute("is_admin", true);
        } else {
            model.addAttribute("is_admin", false);
        }

        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("gym", gym.get());
        model.addAttribute("logs", logs);
        model.addAttribute("logsGroupedByDate", logsGroupedByDate);
        model.addAttribute("routes", routes);
        model.addAttribute("mainServerUrl", mainServerUrl);

        return new ModelAndView("logbook");
    }

    @GetMapping("/get_ranking/{gymId}")
    @RequiresPermission({ "VIEWER" })
    public ModelAndView getRanking(@PathVariable String gymId, Model model,
            HttpSession session) {

        List<String> permissions = (List<String>) session.getAttribute("user_permissions");
        Optional<Gym> gym = gymService.findById(gymId);

        if (!gym.isPresent()) {
            return new ModelAndView("error");
        }

        User loggedInUser = entityService.findLoggedInUserOrFail(gymId, session);

        List<User> users = userService.getAllUsersByGymIdAndIsActive(gymId);

        List<User> sortedUsers = users.stream()
                .sorted(Comparator.comparing(User::getPoints).reversed())
                .collect(Collectors.toList());

        int loggedInUserRank = 0;
        for (int i = 0; i < sortedUsers.size(); i++) {
            if (sortedUsers.get(i).getUid().equals(loggedInUser.getUid())) {
                loggedInUserRank = i + 1; // ranks are 1-based
                break;
            }
        }

        if (permissions.contains("ADMIN")) {
            model.addAttribute("is_admin", true);
        } else {
            model.addAttribute("is_admin", false);
        }

        model.addAttribute("loggedInUserRank", loggedInUserRank);
        model.addAttribute("sortedUsers", sortedUsers);
        model.addAttribute("gym", gym.get());
        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("mainServerUrl", mainServerUrl);
        return new ModelAndView("ranking");
    }

    @GetMapping("/get_admin/{gymId}")
    @RequiresPermission({ "ADMIN" })
    public ModelAndView getAdmin(@PathVariable String gymId, Model model,
            HttpSession session) {
                
        List<String> permissions = (List<String>) session.getAttribute("user_permissions");
        Optional<Gym> gym = gymService.findById(gymId);

        if (!gym.isPresent()) {
            return new ModelAndView("error");
        }

        User loggedInUser = entityService.findLoggedInUserOrFail(gymId, session);

        // Fetch active walls
        List<Wall> activeWalls = wallService.findAllActiveWalls(gymId);

        // Fetch routes on active walls
        List<Route> routes = new ArrayList<>();
        for (Wall wall : activeWalls) {
            routes.addAll(routeService.findAllByWallId(wall.getId()));
        }
        System.out.println("Routes" + routes);

        if (permissions.contains("ADMIN")) {
            model.addAttribute("is_admin", true);
        } else {
            model.addAttribute("is_admin", false);
        }

        model.addAttribute("activeWalls", activeWalls);
        model.addAttribute("routes", routes);
        model.addAttribute("gym", gym.get());
        model.addAttribute("loggedInUser", loggedInUser);
        return new ModelAndView("adminPage");
    }
}
