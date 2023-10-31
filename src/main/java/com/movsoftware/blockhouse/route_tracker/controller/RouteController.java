package com.movsoftware.blockhouse.route_tracker.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.movsoftware.blockhouse.route_tracker.entities.Comment;
import com.movsoftware.blockhouse.route_tracker.entities.Gym;
import com.movsoftware.blockhouse.route_tracker.entities.Route;
import com.movsoftware.blockhouse.route_tracker.entities.User;
import com.movsoftware.blockhouse.route_tracker.entities.Wall;
import com.movsoftware.blockhouse.route_tracker.pojo.BoulderingStyle;
import com.movsoftware.blockhouse.route_tracker.security.RequiresPermission;
import com.movsoftware.blockhouse.route_tracker.service.EntityService;
import com.movsoftware.blockhouse.route_tracker.service.GymService;
import com.movsoftware.blockhouse.route_tracker.service.RouteService;
import com.movsoftware.blockhouse.route_tracker.service.WallService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/route")
public class RouteController {

    @Value("${main.server.url}")
    private String mainServerUrl;

    private final RouteService routeService;
    private final WallService wallService;
    private final GymService gymService;
    private final EntityService entityService;

    public RouteController(RouteService routeService, WallService WallService, GymService gymService,
            EntityService entityService) {
        this.routeService = routeService;
        this.wallService = WallService;
        this.gymService = gymService;
        this.entityService = entityService;
    }

    @GetMapping("/ping")
    public String ping() {
        return "Pong!";
    }

    @GetMapping("/get_route")
    public ModelAndView getRoute(@RequestParam String routeId, @RequestParam String gymId, Model model,
            HttpSession session) {

        User loggedInUser = entityService.findLoggedInUserOrFail(gymId, session);

        Optional<Gym> gym = gymService.findById(gymId);
        if (!gym.isPresent()) {
            return new ModelAndView("/error");
        }

        Optional<Route> optionalRoute = routeService.getRouteById(routeId);

        if (!optionalRoute.isPresent()) {
            return new ModelAndView("error");
        }

        Route route = optionalRoute.get();

        List<Comment> comments = route.getComments();

        model.addAttribute("gym", gym.get());
        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("route", route);
        model.addAttribute("comments", comments);
        return new ModelAndView("route");

    }

    @PostMapping("/save_route")
    @RequiresPermission({ "ADMIN" })
    public ModelAndView saveRoute(@RequestParam String name,
            @RequestParam String setter,
            @RequestParam String grade,
            @RequestParam String wallId,
            @RequestParam String gymId,
            @RequestParam String style,
            @RequestParam String description,
            RedirectAttributes redirectAttributes,
            HttpSession session) {

        entityService.findLoggedInUserOrFail(gymId, session);
        Optional<Gym> optionalGym = gymService.findById(gymId);
        Optional<Wall> optionalWall = wallService.findWallById(wallId);

        if (optionalGym.isPresent() && optionalWall.isPresent()) {
            // Gym gym = optionalGym.get();
            Wall wall = optionalWall.get();

            // You may need additional validations here, like checking if the Wall belongs
            // to the Gym
            BoulderingStyle boulderingStyle = null;
            for (BoulderingStyle value : BoulderingStyle.values()) {
                if (value.name().equalsIgnoreCase(style)) {
                    boulderingStyle = value;
                    break;
                }
            }

            if (boulderingStyle == null) {
                redirectAttributes.addFlashAttribute("updateMessage", "Invalid Style");
                return new ModelAndView("redirect:/nav/get_admin/" + gymId);
            }

            Route newRoute = new Route(name, setter, grade, boulderingStyle, description);

            newRoute.setWall(wall);
            wall.getRoutes().add(newRoute); // Add the new route to the wall's list of routes

            Route savedRoute = routeService.saveRoute(newRoute);

            if (savedRoute != null) {
                wallService.saveWall(wall); // Update the wall to persist the new list of routes
                redirectAttributes.addFlashAttribute("updateMessage", "Route Saved!");
                return new ModelAndView("redirect:/nav/get_admin/" + gymId);
            } else {
                redirectAttributes.addFlashAttribute("updateMessage", "Failed to save route.");
                return new ModelAndView("redirect:/nav/get_admin/" + gymId);
            }
        } else {
            redirectAttributes.addFlashAttribute("updateMessage", "Gym or Wall not found.");
            return new ModelAndView("redirect:/nav/get_admin/" + gymId);
        }
    }

    @PostMapping("/update_route")
    @RequiresPermission({ "ADMIN" })
    public ModelAndView updateRoute(
            @RequestParam String routeId,
            @RequestParam String gymId,
            @RequestParam String wallId,
            @RequestParam String firstAscent,
            @RequestParam String name,
            @RequestParam String setterName,
            @RequestParam String grade,
            @RequestParam String style,
            @RequestParam String description,
            @RequestParam(required = false) String isActive,
            HttpSession session, RedirectAttributes redirectAttributes) {

        Optional<Gym> gym = gymService.findById(gymId);

        if (!gym.isPresent()) {
            return new ModelAndView("error");
        }

        entityService.findLoggedInUserOrFail(gymId, session);

        // Validate that the route exists
        Optional<Route> optionalRoute = routeService.getRouteById(routeId);
        if (!optionalRoute.isPresent()) {
            redirectAttributes.addFlashAttribute("updateMessage", "Route not found");
            return new ModelAndView("redirect:/error");
        }

        Route route = optionalRoute.get();

        // Update route properties
        if (name != null && !name.isEmpty()) {
            route.setName(name);
        }

        route.setIsActive("on".equals(isActive));

        if (wallId != null && !wallId.isEmpty()) {
            Optional<Wall> optionalNewWall = wallService.findWallById(wallId);
            if (optionalNewWall.isPresent()) {
                Wall newWall = optionalNewWall.get();
    
                // Get the old wall
                Wall oldWall = route.getWall();
    
                // Remove route from the old wall's routes
                if (oldWall != null) {
                    oldWall.getRoutes().remove(route);
                }
    
                // Add route to the new wall's routes
                newWall.getRoutes().add(route);
    
                // Set new wall on the route
                route.setWall(newWall);
            }
        }

        if (firstAscent != null && !firstAscent.isEmpty()) {
            route.setFirstAscent(firstAscent);
        }
        if (setterName != null && !setterName.isEmpty()) {
            route.setSetterName(setterName);
        }
        if (grade != null && !grade.isEmpty()) {
            route.setGrade(grade);
        }
        if (style != null && !style.isEmpty()) {
            route.setStyle(BoulderingStyle.valueOf(style)); // Assume BoulderingStyle is an Enum
        }
        if (description != null && !description.isEmpty()) {
            route.setDescription(description);
        }
        
        // Save the updated route
        routeService.saveRoute(route);

        redirectAttributes.addFlashAttribute("updateMessage", "Route Updated!");
        return new ModelAndView("redirect:/nav/get_admin/" + gymId);
    }

}
