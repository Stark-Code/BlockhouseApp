package com.movsoftware.blockhouse.route_tracker.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.movsoftware.blockhouse.route_tracker.dto.SyncUserRequest;
import com.movsoftware.blockhouse.route_tracker.entities.Gym;
import com.movsoftware.blockhouse.route_tracker.entities.User;
import com.movsoftware.blockhouse.route_tracker.notification.NotificationService;
import com.movsoftware.blockhouse.route_tracker.pojo.SortBy;
import com.movsoftware.blockhouse.route_tracker.security.InvitationCodeGenerator;
import com.movsoftware.blockhouse.route_tracker.security.RequiresPermission;
import com.movsoftware.blockhouse.route_tracker.service.EntityService;
import com.movsoftware.blockhouse.route_tracker.service.GymService;
import com.movsoftware.blockhouse.route_tracker.service.UserService;
import com.movsoftware.blockhouse.route_tracker.service.ValidationService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
public class UserController {

    @Value("${main.server.url}")
    private String mainServerUrl;

    private final EntityService entityService;
    private final UserService userService;
    private final GymService gymService;
    private final ValidationService validationService;
    private final NotificationService notificationService;

    public UserController(EntityService entityService, UserService userService, GymService gymService,
            ValidationService validationService, NotificationService notificationService) {
        this.entityService = entityService;
        this.userService = userService;
        this.gymService = gymService;
        this.validationService = validationService;
        this.notificationService = notificationService;
    }

    @GetMapping("/ping")
    public String ping() {
        return "Pong!";
    }

    @PostMapping("/update_sort_by")
    public ModelAndView updateSortBy(@RequestParam String strSortBy, @RequestParam String gymId, HttpSession session,
            RedirectAttributes redirectAttributes) {

        User loggedInUser = entityService.findLoggedInUserOrFail(gymId, session);

        Optional<Gym> gym = gymService.findById(gymId);
        if (!gym.isPresent()) {
            return new ModelAndView("/error");
        }

        SortBy sortBy = SortBy.valueOf(strSortBy); // Convert the string to the enum
        loggedInUser.setSortBy(sortBy);
        userService.saveUser(loggedInUser);

        return new ModelAndView("redirect:/nav/get_route_explorer/" + gymId);
    }

    // User first created on Firebase, add user to database first time they sign in
    @PostMapping("/sync_user") // Status: Checked
    public ResponseEntity<User> syncUser(@RequestBody SyncUserRequest request) {

        System.out.println("Entering syncUser method"); // Print statement added
        System.out.println(request);

        String gymId = request.getGymId();
        Gym gym = entityService.findGymOrFail(gymId);

        String uid = request.getUid();
        String email = request.getEmail();
        String name = request.getName();
        Set<String> permissions = request.getPermissions();

        System.out.println("User Details: "); // Print statement added
        System.out.println("UID: " + uid); // Print statement added
        System.out.println("Email: " + email); // Print statement added
        System.out.println("Name: " + name); // Print statement added
        System.out.println("Permissions: " + permissions); // Print statement added

        User user; // Declare user here

        // Check if Active user already exists (Checks isActive = true)
        Optional<User> userOpt = userService.getUserByUidAndGymId(uid, gymId);
        if (!userOpt.isPresent()) {
            System.out.println("User not present, creating new user"); // Print statement added

            user = new User();
            user.setUid(uid);
            user.setEmail(email);
            user.setName(name);
            user.setPermissions(permissions);
            user.setGym(gym);
            user.setSortBy(SortBy.EASIEST);
            user = userService.saveUser(user);

            System.out.println("User created: " + user); // Print statement added
        } else {
            user = userOpt.get(); // If the user already exists, assign it to user
            System.out.println("User already exists: " + user); // Print statement added
        }

        System.out.println("Exiting syncUser method"); // Print statement added

        return ResponseEntity.ok(user);
    }

    @PostMapping("/invite_user") // Status: Production Ready
    @RequiresPermission({ "ADMIN" })
    public ModelAndView invite_user(@RequestParam("gymId") String gymId,
            @RequestParam("email") String email, HttpSession session, RedirectAttributes redirectAttributes) {

        Optional<Gym> gym = gymService.findById(gymId);

        if (!gym.isPresent()) {
            redirectAttributes.addFlashAttribute("updateMessage", "Invitation failed");
            return new ModelAndView("redirect:/nav/get_admin/" + gymId);
        }

        entityService.findLoggedInUserOrFail(gymId, session);

        if (!validationService.validateEmailFormat(email)) {
            redirectAttributes.addFlashAttribute("updateMessage", "Invitation failed, invalid email");
            return new ModelAndView("redirect:/nav/get_admin/" + gymId);
        }

        String invitationCode = InvitationCodeGenerator.generateInvitationCode(email, gymId);
        boolean sendEmail = notificationService.sendInvitation(email, invitationCode, gym.get().getName());
        if (sendEmail) {
            redirectAttributes.addFlashAttribute("updateMessage", "Invitation sent");
            return new ModelAndView("redirect:/nav/get_admin/" + gymId);
        } else {
            redirectAttributes.addFlashAttribute("updateMessage", "Invitation failed");
            return new ModelAndView("redirect:/nav/get_admin/" + gymId);
        }
    }

    // Requests to this endpoint made from authorization server
    @PostMapping("/check_registration") // Status: Checked
    public ResponseEntity<?> decodeToken(@RequestBody Map<String, Object> payload) {
        String registrationCode = (String) payload.get("registrationCode");
        String email = (String) payload.get("email");

        if (registrationCode == null || registrationCode.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No registration code provided");
        }

        if (email == null || email.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No email provided");
        }

        try {
            Jws<Claims> claims = InvitationCodeGenerator.decodeInvitationCode(registrationCode);
            if (!claims.getBody().get("email").equals(email)) {
                System.out.println(email + " is not authorized to register for this organization");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Registration Code");
            }

            // Check the permissions claim
            @SuppressWarnings("unchecked")
            List<String> permissions = (List<String>) claims.getBody().get("permissions");
            if (permissions == null || !permissions.contains("register")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Insufficient permissions");
            }

            Map<String, Object> response = new HashMap<>();
            response.put("valid", true);
            response.put("organizationId", claims.getBody().get("organizationId"));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("Error" + e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid JWT");
        }
    }
}
