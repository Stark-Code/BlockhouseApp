package com.movsoftware.blockhouse.route_tracker.service_implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.movsoftware.blockhouse.route_tracker.entities.User;
import com.movsoftware.blockhouse.route_tracker.repository.UserRepository;
import com.movsoftware.blockhouse.route_tracker.service.UserService;

import jakarta.servlet.http.HttpSession;

@Service
public class UserServiceImplementation implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> getUserByUidAndGymId(String uid, String gymId) {
        return userRepository.findByUidAndGym_IdAndIsActiveIsTrue(uid, gymId);
    }

    @Override
    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    @Override
    public List<User> getAllUsersByGymIdAndIsActive(String gymId) {
        return userRepository.findAllByGym_IdAndIsActiveIsTrue(gymId);
    }

    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    // @Override
    public Optional<User> getUserByUidAndgymId(String uid, String gymId) {
        return userRepository.findByUidAndGym_IdAndIsActiveIsTrue(uid, gymId);
    }
    
    public User findLoggedInUser(HttpSession session, String gymId) {
        System.out.println("Entering findLoggedInUser method");
        String uid = (String) session.getAttribute("uid");
        System.out.println("Gym Id: " + gymId);
        System.out.println("UID: " + uid);
        Optional<User> loggedInUserOpt = getUserByUidAndgymId(uid, gymId);
        
        if (loggedInUserOpt.isPresent()) {
            return loggedInUserOpt.get();
        } else {
            return null;
        }
    }
}
