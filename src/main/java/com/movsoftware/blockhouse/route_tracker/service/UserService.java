package com.movsoftware.blockhouse.route_tracker.service;

import java.util.List;
import java.util.Optional;

import com.movsoftware.blockhouse.route_tracker.entities.User;

import jakarta.servlet.http.HttpSession;

public interface UserService {
    User saveUser(User user);

    Optional<User> getUserById(String id);

    Optional<User> getUserByUidAndGymId(String uid, String gymId);

    List<User> getAllUsersByGymIdAndIsActive(String gymId);

    List<User> getAllUsers();

    User updateUser(User user);

    void deleteUser(String id);

    User findLoggedInUser(HttpSession session, String gymId);

}
