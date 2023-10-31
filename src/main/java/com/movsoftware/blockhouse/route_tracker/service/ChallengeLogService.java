package com.movsoftware.blockhouse.route_tracker.service;

import java.util.List;
import java.util.Optional;

import com.movsoftware.blockhouse.route_tracker.entities.ChallengeLog;
import com.movsoftware.blockhouse.route_tracker.entities.User;

public interface ChallengeLogService {
    ChallengeLog saveChallengeLog(ChallengeLog challengeLog);
    List<ChallengeLog> getAllChallengeLogs();
    Optional<ChallengeLog> getChallengeLogById(String id);
    ChallengeLog createChallengeLog(ChallengeLog challengeLog);
    ChallengeLog updateChallengeLog(String id, ChallengeLog challengeLog);
    void checkAllChallenges(User user);
}

