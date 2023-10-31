package com.movsoftware.blockhouse.route_tracker.service_implementation;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.movsoftware.blockhouse.route_tracker.entities.Gym;
import com.movsoftware.blockhouse.route_tracker.entities.User;
import com.movsoftware.blockhouse.route_tracker.exceptions.GymNotFound;
import com.movsoftware.blockhouse.route_tracker.exceptions.UserNotFound;
import com.movsoftware.blockhouse.route_tracker.service.EntityService;
import com.movsoftware.blockhouse.route_tracker.service.GymService;
import com.movsoftware.blockhouse.route_tracker.service.UserService;

import jakarta.servlet.http.HttpSession;

@Service
public class EntityServiceImplementation implements EntityService {

    private final GymService gymService;
    private final UserService userService;

    public EntityServiceImplementation(GymService gymService, UserService userService) {
        this.gymService = gymService;
        this.userService = userService;
    }

    @Override
    public Gym findGymOrFail(String gymId) {

        Optional<Gym> optionalGym = gymService.findById(gymId);

        if (optionalGym.isPresent()) {
            return optionalGym.get();
        } else {
            throw new GymNotFound("Gym with ID " + gymId + " not found");
        }
    }

    @Override
    public User findLoggedInUserOrFail(String gymId, HttpSession session) {

        User user = userService.findLoggedInUser(session, gymId);
        if (user == null) {
            throw new UserNotFound("User not found");
        }
        return user;
    }
}
