package com.movsoftware.blockhouse.route_tracker.service_implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.movsoftware.blockhouse.route_tracker.entities.Route;
import com.movsoftware.blockhouse.route_tracker.repository.RouteRepository;
import com.movsoftware.blockhouse.route_tracker.service.RouteService;

@Service
public class RouteServiceImplementation implements RouteService {
    
    @Autowired
    private RouteRepository routeRepository;
    
    @Override
    public Route saveRoute(Route route) {
        return routeRepository.save(route);
    }
    
    @Override
    public Optional<Route> getRouteById(String id) {
        return routeRepository.findById(id);
    }
    
    @Override
    public List<Route> getAllRoutes() {
        return (List<Route>) routeRepository.findAll();
    }
    
    @Override
    public void deleteRoute(String id) {
        routeRepository.deleteById(id);
    }

    @Override
    public List<Route> findAllByWallId(String wallId) {
        return routeRepository.findAllByWallId(wallId);
    }
}
