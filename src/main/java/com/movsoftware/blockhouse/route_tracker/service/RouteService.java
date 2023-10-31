package com.movsoftware.blockhouse.route_tracker.service;

import java.util.List;
import java.util.Optional;

import com.movsoftware.blockhouse.route_tracker.entities.Route;

public interface RouteService {
    Route saveRoute(Route route);
    Optional<Route> getRouteById(String id);
    List<Route> getAllRoutes();
    void deleteRoute(String id);
    List<Route> findAllByWallId(String wallId);
}
