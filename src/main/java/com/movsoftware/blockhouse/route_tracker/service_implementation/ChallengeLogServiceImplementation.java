package com.movsoftware.blockhouse.route_tracker.service_implementation;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.movsoftware.blockhouse.route_tracker.entities.Challenge;
import com.movsoftware.blockhouse.route_tracker.entities.ChallengeLog;
import com.movsoftware.blockhouse.route_tracker.entities.Log;
import com.movsoftware.blockhouse.route_tracker.entities.User;
import com.movsoftware.blockhouse.route_tracker.exceptions.ValidationException;
import com.movsoftware.blockhouse.route_tracker.repository.ChallengeLogRepository;
import com.movsoftware.blockhouse.route_tracker.service.ChallengeLogService;
import com.movsoftware.blockhouse.route_tracker.service.ChallengeService;

@Service
public class ChallengeLogServiceImplementation implements ChallengeLogService {

    @Autowired
    private ChallengeLogRepository challengeLogRepository;

    @Autowired
    private ChallengeService challengeService;

    @Override
    public ChallengeLog saveChallengeLog(ChallengeLog challengeLog) {
        return challengeLogRepository.save(challengeLog);
    }

    @Override
    public List<ChallengeLog> getAllChallengeLogs() {
        return challengeLogRepository.findAll();
    }

    @Override
    public Optional<ChallengeLog> getChallengeLogById(String id) {
        return challengeLogRepository.findById(id);
    }

    @Override
    public ChallengeLog createChallengeLog(ChallengeLog challengeLog) {
        return challengeLogRepository.save(challengeLog);
    }

    @Override
    public ChallengeLog updateChallengeLog(String id, ChallengeLog challengeLog) {
        if (challengeLogRepository.existsById(id)) {
            // challengeLog.setId(id);
            return challengeLogRepository.save(challengeLog);
        } else {
            throw new ValidationException("ChallengeLog not found with id: " + id);
        }
    }

    @Override
    public void checkAllChallenges(User user) {

        System.out.println("Checking all challenges for user: " + user.getName());

        List<ChallengeLog> challengesLog = user.getChallengesLog();

        // Check if climber has logged 1 unique ascent
        Optional<Challenge> challengeOpt = challengeService.getChallengeByName("One Unique Ascent");
        Challenge challenge = challengeOpt.get();
        // Check if user has already completed the challenge
        if (!hasCompletedChallenge(challengesLog, challenge)) {
            if (isUniqueAscentsChallengeCompleted(user, 1)) {
                System.out.println("User has completed the One Unique Ascent challenge");
                ChallengeLog challengeLog = new ChallengeLog(user, challenge, LocalDate.now());
                user.addChallengeLog(challengeLog);
                saveChallengeLog(challengeLog);
            }
        }

        // if (isUniqueAscentsChallengeCompleted(user, 5)) {

        // }
        // if (isUniqueAscentsChallengeCompleted(user, 25)) {

        // }
        // if (isUniqueAscentsChallengeCompleted(user, 50)) {

        // }
        // if (isUniqueAscentsChallengeCompleted(user, 100)) {

        // }
        // if (isUniqueAscentsChallengeCompleted(user, 200)) {

        // }
        // if (isUniqueAscentsChallengeCompleted(user, 300)) {

        // }
        // if (isUniqueAscentsChallengeCompleted(user, 400)) {

        // }
        // if (isUniqueAscentsChallengeCompleted(user, 500)) {

        // }
    }

    // Challenge Checkers and Helpers

    // Check if user has completed a challenge
    public boolean hasCompletedChallenge(List<ChallengeLog> challengeLogs, Challenge targetChallenge) {

        for (ChallengeLog log : challengeLogs) {
            Challenge existingChallenge = log.getChallenge();
            if (existingChallenge.getId().equals(targetChallenge.getId())) {
                return true;
            }
        }
        return false;
    }

    public boolean isUniqueAscentsChallengeCompleted(User user, int targetCount) {
        System.out.println("------ Start: Checking if user has completed the " + targetCount + " unique ascents challenge ------");
    
        List<Log> logbook = user.getLogbook();
        
        System.out.println("User's logbook size: " + logbook.size());
    
        Set<String> uniqueAscents = new HashSet<>();
    
        for (Log log : logbook) {
            String ascentId = log.getRoute().getId();
            System.out.println("Adding ascentId to set: " + ascentId);
            uniqueAscents.add(ascentId);
        }
    
        System.out.println("Number of unique ascents: " + uniqueAscents.size());
    
        boolean isCompleted = uniqueAscents.size() >= targetCount;
    
        if (isCompleted) {
            System.out.println("User has completed the challenge.");
        } else {
            System.out.println("User has NOT completed the challenge.");
        }
    
        System.out.println("------ End: Checking if user has completed the " + targetCount + " unique ascents challenge ------");
    
        return isCompleted;
    }
    

}

