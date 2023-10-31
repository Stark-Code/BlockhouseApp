package com.movsoftware.blockhouse.route_tracker.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.movsoftware.blockhouse.route_tracker.entities.Route;

public interface RouteRepository extends CrudRepository<Route, String> {
    List<Route> findAllByWallId(String wallId);
}
