package com.movsoftware.blockhouse.route_tracker.service;

import java.util.List;
import java.util.Optional;

import com.movsoftware.blockhouse.route_tracker.entities.Challenge;

public interface ChallengeService {
    List<Challenge> getAllChallenges();
    Optional<Challenge> getChallengeById(String id);
    Optional<Challenge> getChallengeByName(String name);
    Challenge saveChallenge(Challenge challenge);
    Challenge updateChallenge(String id, Challenge challenge);
}

