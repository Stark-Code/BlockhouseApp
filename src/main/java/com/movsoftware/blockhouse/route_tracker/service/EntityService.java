package com.movsoftware.blockhouse.route_tracker.service;

import com.movsoftware.blockhouse.route_tracker.entities.Gym;
import com.movsoftware.blockhouse.route_tracker.entities.User;

import jakarta.servlet.http.HttpSession;

public interface EntityService {
    Gym findGymOrFail(String gymId);
    User findLoggedInUserOrFail(String gymId, HttpSession session);
}
