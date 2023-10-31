package com.movsoftware.blockhouse.route_tracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.movsoftware.blockhouse.route_tracker.entities.Log;

public interface LogRepository extends JpaRepository<Log, Long> {
    List<Log> findByUserUid(String userId);
}

