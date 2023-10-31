package com.movsoftware.blockhouse.route_tracker.service;

import java.util.List;
import java.util.Optional;

import com.movsoftware.blockhouse.route_tracker.entities.Gym;

public interface GymService {
    Gym saveGym(Gym gym);
    Optional<Gym> findById(String id);
    List<Gym> findAllGyms();
    void deleteGymById(String id);
}
