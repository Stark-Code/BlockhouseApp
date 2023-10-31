package com.movsoftware.blockhouse.route_tracker.repository;

import org.springframework.data.repository.CrudRepository;

import com.movsoftware.blockhouse.route_tracker.entities.Gym;

public interface GymRepository extends CrudRepository<Gym, String> {

}

