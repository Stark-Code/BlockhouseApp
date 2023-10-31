package com.movsoftware.blockhouse.route_tracker.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.movsoftware.blockhouse.route_tracker.entities.Gym;
import com.movsoftware.blockhouse.route_tracker.entities.Wall;
import com.movsoftware.blockhouse.route_tracker.security.RequiresPermission;
import com.movsoftware.blockhouse.route_tracker.service.EntityService;
import com.movsoftware.blockhouse.route_tracker.service.GymService;
import com.movsoftware.blockhouse.route_tracker.service.WallService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/wall")
public class WallController {

    @Value("${main.server.url}")
    private String mainServerUrl;

    @GetMapping("/ping")
    public String ping() {
        return "Pong!";
    }

    private final GymService gymService;
    private final WallService wallService;
    private final EntityService entityService;

    WallController(GymService gymService, WallService wallService, EntityService entityService) {
        this.gymService = gymService;
        this.wallService = wallService;
        this.entityService = entityService;
    }

    @PostMapping("/save_wall")
    @RequiresPermission({ "ADMIN" })
    public ModelAndView saveWall(@RequestParam String name, @RequestParam String gymId,
            RedirectAttributes redirectAttributes) {
        Optional<Gym> optionalGym = gymService.findById(gymId);

        if (optionalGym.isPresent()) {
            Gym gym = optionalGym.get();

            Wall newWall = new Wall(name);
            newWall.setGym(gym);

            // Add the new wall to the gym's list of walls if needed
            // This assumes that the gym's list of walls is not null.
            // If the list can be null, you should check and initialize it.
            gym.getWalls().add(newWall);

            Wall savedWall = wallService.saveWall(newWall);

            if (savedWall != null) {
                gymService.saveGym(gym); // Update the gym to persist the new list of walls
                redirectAttributes.addFlashAttribute("updateMessage", "Wall Saved!");
                return new ModelAndView("redirect:/nav/get_admin/" + gymId);
            } else {
                redirectAttributes.addFlashAttribute("updateMessage", "Failed to save wall.");
                return new ModelAndView("redirect:/nav/get_admin/" + gymId);
            }
        } else {
            redirectAttributes.addFlashAttribute("updateMessage", "Gym not found");
            return new ModelAndView("redirect:/nav/get_route_explorer/" + gymId);
        }
    }

    @PostMapping("/update_wall")
    @RequiresPermission({ "ADMIN" })
    public ModelAndView updateWall(
            @RequestParam String name,
            @RequestParam(required = false) String isActive,
            @RequestParam String wallId,
            @RequestParam String gymId,
            HttpSession session, RedirectAttributes redirectAttributes) {
        
        System.out.println("isActive: " + isActive);
        // Validate that the user is logged in and has permissions for the gym
        entityService.findLoggedInUserOrFail(gymId, session);

        // Validate that the wall exists
        Optional<Wall> optionalWall = wallService.findWallById(wallId);
        if (!optionalWall.isPresent()) {
            redirectAttributes.addFlashAttribute("updateMessage", "Wall not found");
                return new ModelAndView("redirect:/nav/get_route_explorer/" + gymId);
        }

        Wall wall = optionalWall.get();

        // Update wall properties
        wall.setName(name);

        wall.setIsActive("on".equals(isActive));

        // Save the updated wall
        wallService.saveWall(wall);

        redirectAttributes.addFlashAttribute("updateMessage", "Wall Updated!");
                return new ModelAndView("redirect:/nav/get_admin/" + gymId);
    }
}
