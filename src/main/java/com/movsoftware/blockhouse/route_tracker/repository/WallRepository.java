package com.movsoftware.blockhouse.route_tracker.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.movsoftware.blockhouse.route_tracker.entities.Wall;

public interface WallRepository extends CrudRepository<Wall, String> {
    List<Wall> findAllByGymId(String gymId);
    List<Wall> findAllByGymIdAndIsActive(String gymId, Boolean isActive);
}

