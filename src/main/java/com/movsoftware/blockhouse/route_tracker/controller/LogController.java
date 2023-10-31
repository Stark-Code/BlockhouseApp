package com.movsoftware.blockhouse.route_tracker.controller;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.movsoftware.blockhouse.route_tracker.entities.Comment;
import com.movsoftware.blockhouse.route_tracker.entities.Gym;
import com.movsoftware.blockhouse.route_tracker.entities.Log;
import com.movsoftware.blockhouse.route_tracker.entities.Route;
import com.movsoftware.blockhouse.route_tracker.entities.User;
import com.movsoftware.blockhouse.route_tracker.pojo.Grade;
import com.movsoftware.blockhouse.route_tracker.pojo.NumberOfTries;
import com.movsoftware.blockhouse.route_tracker.service.ChallengeLogService;
import com.movsoftware.blockhouse.route_tracker.service.EntityService;
import com.movsoftware.blockhouse.route_tracker.service.GymService;
import com.movsoftware.blockhouse.route_tracker.service.LogService;
import com.movsoftware.blockhouse.route_tracker.service.RouteService;
import com.movsoftware.blockhouse.route_tracker.service.UserService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/log")
public class LogController {

    @Value("${main.server.url}")
    private String mainServerUrl;

    private final RouteService routeService;
    private final GymService gymService;
    private final EntityService entityService;
    private final UserService userService;
    private final LogService logService;
    private final ChallengeLogService challengeLogService;

    public LogController(RouteService routeService, GymService gymService,
            EntityService entityService, UserService userService, LogService logService,
            ChallengeLogService challengeLogService) {
        this.routeService = routeService;
        this.gymService = gymService;
        this.entityService = entityService;
        this.userService = userService;
        this.logService = logService;
        this.challengeLogService = challengeLogService;
    }

    @GetMapping("/ping")
    public String ping() {
        return "Pong!";
    }

    @PostMapping("/save_log")
    public ModelAndView saveLog(
            @RequestParam("numberOfTries") String numberOfTries,
            @RequestParam("rating") int rating,
            @RequestParam("comment") String commentText,
            @RequestParam("routeId") String routeId,
            @RequestParam("gymId") String gymId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        // Validate presence of user, gym, and route
        User loggedInUser = entityService.findLoggedInUserOrFail(gymId, session);
        Optional<Gym> gym = gymService.findById(gymId);
        Optional<Route> optional_route = routeService.getRouteById(routeId);

        if (!gym.isPresent() || !optional_route.isPresent()) {
            return new ModelAndView("error");
        }

        Route route = optional_route.get();

        // Update route rating
        double newAverageRating = ((route.getTotalSends() * route.getRating()) + rating) / (route.getTotalSends() + 1);
        route.setRating(newAverageRating);

        // Add comment if available
        if (commentText != null && !commentText.isBlank()) {
            LocalDateTime now = LocalDateTime.now();
            Comment comment = new Comment(commentText, now, route, loggedInUser.getName());
            route.addComment(comment);
        }

        // Update total sends for the route
        route.setTotalSends(route.getTotalSends() + 1);

        // Update first ascent if needed
        if (route.getFirstAscent().equals("N/A")) {
            route.setFirstAscent(loggedInUser.getName());
        }

        // Save the route after updates
        routeService.saveRoute(route);

        // Update user points and logbook
        boolean firstTimeLogging = true;
        for (Log previousLog : loggedInUser.getLogbook()) {
            if (previousLog.getRoute().getId().equals(routeId)) {
                firstTimeLogging = false;
                break;
            }
        }

        if (firstTimeLogging) {
            Grade grade = Grade.valueOf(route.getGrade());
            int basePoints = grade.getBasePoints();
            float multiplier = NumberOfTries.valueOf(numberOfTries).getMultiplier();
            int points = (int) (basePoints * multiplier);
            loggedInUser.incrementPoints(points);
        }

        // Create and save new log entry
        Log log = new Log(numberOfTries, rating, route, loggedInUser);
        logService.saveLog(log);

        // Add new log entry to user's logbook and save user
        loggedInUser.addLog(log);
        userService.saveUser(loggedInUser);

        // challengeLogService.checkAllChallenges(loggedInUser);

        // Redirect with success message
        redirectAttributes.addFlashAttribute("updateMessage", "Update Success!");
        return new ModelAndView("redirect:/nav/get_route_explorer/" + gymId);
    }

}