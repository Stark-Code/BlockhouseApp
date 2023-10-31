package com.movsoftware.blockhouse.route_tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.movsoftware.blockhouse.route_tracker.entities.ChallengeLog;

public interface ChallengeLogRepository extends JpaRepository<ChallengeLog, String> {
    
}

