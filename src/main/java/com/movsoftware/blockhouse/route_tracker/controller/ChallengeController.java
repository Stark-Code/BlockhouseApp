package com.movsoftware.blockhouse.route_tracker.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.movsoftware.blockhouse.route_tracker.entities.Challenge;
import com.movsoftware.blockhouse.route_tracker.entities.Gym;
import com.movsoftware.blockhouse.route_tracker.security.RequiresPermission;
import com.movsoftware.blockhouse.route_tracker.service.ChallengeService;
import com.movsoftware.blockhouse.route_tracker.service.GymService;

@RestController
@RequestMapping("/challenge")
public class ChallengeController {

    @Autowired
    private ChallengeService challengeService;

    @Autowired
    private GymService gymService;

    // Create a new challenge
    @PostMapping("/save_challenge")
    @RequiresPermission({ "ADMIN" })
    public ResponseEntity<Challenge> createChallenge(@RequestParam String name, @RequestParam String description,
            @RequestParam String isOngoing, @RequestParam String isPeriodic, @RequestParam String gymId) {

        Optional<Gym> optionalGym = gymService.findById(gymId);

        if (!optionalGym.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Boolean isOngoingBool = Boolean.parseBoolean(isOngoing);
        Boolean isPeriodicBool = Boolean.parseBoolean(isPeriodic);

        Challenge challenge = new Challenge(name, description, isOngoingBool, isPeriodicBool);
        challenge.setGym(optionalGym.get());        
        Challenge savedChallenge = challengeService.saveChallenge(challenge);
        return new ResponseEntity<>(savedChallenge, HttpStatus.CREATED);
    }

    // Retrieve all challenges
    @GetMapping("/get_all_challenges")
    public ResponseEntity<List<Challenge>> getAllChallenges() {
        List<Challenge> challenges = challengeService.getAllChallenges();
        return new ResponseEntity<>(challenges, HttpStatus.OK);
    }

    // Retrieve a single challenge by ID
    @GetMapping("/get_challenge/{id}")
    public ResponseEntity<Challenge> getChallengeById(@PathVariable String id) {
        Optional<Challenge> challenge = challengeService.getChallengeById(id);
        if (challenge.isPresent()) {
            return new ResponseEntity<>(challenge.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Update a challenge
    @PutMapping("/update_challenge/{id}")
    @RequiresPermission({ "ADMIN" })
    public ResponseEntity<Challenge> updateChallenge(@PathVariable String id, @RequestBody Challenge challenge) {
        Optional<Challenge> existingChallenge = challengeService.getChallengeById(id);
        if (existingChallenge.isPresent()) {
            Challenge updatedChallenge = challengeService.saveChallenge(challenge);
            return new ResponseEntity<>(updatedChallenge, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
