package com.movsoftware.blockhouse.route_tracker.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.movsoftware.blockhouse.route_tracker.entities.Challenge;

public interface ChallengeRepository extends JpaRepository<Challenge, String> {
    Optional<Challenge> findByName(String name);   
}
