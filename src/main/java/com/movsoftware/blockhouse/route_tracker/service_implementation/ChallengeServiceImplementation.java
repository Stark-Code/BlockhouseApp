package com.movsoftware.blockhouse.route_tracker.service_implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.movsoftware.blockhouse.route_tracker.entities.Challenge;
import com.movsoftware.blockhouse.route_tracker.exceptions.ValidationException;
import com.movsoftware.blockhouse.route_tracker.repository.ChallengeRepository;
import com.movsoftware.blockhouse.route_tracker.service.ChallengeService;

@Service
public class ChallengeServiceImplementation implements ChallengeService {

    @Autowired
    private ChallengeRepository challengeRepository;

    @Override
    public List<Challenge> getAllChallenges() {
        return challengeRepository.findAll();
    }

    @Override
    public Optional<Challenge> getChallengeById(String id) {
        return challengeRepository.findById(id);
    }

    @Override
    public Optional<Challenge> getChallengeByName(String name) {
        return challengeRepository.findByName(name);
    }

    @Override
    public Challenge saveChallenge(Challenge challenge) {
        return challengeRepository.save(challenge);
    }

    @Override
    public Challenge updateChallenge(String id, Challenge challenge) {
        if (challengeRepository.existsById(id)) {
            // challenge.setId(id);
            return challengeRepository.save(challenge);
        } else {
            throw new ValidationException("Challenge not found with id: " + id);
        }
    }

    
}
