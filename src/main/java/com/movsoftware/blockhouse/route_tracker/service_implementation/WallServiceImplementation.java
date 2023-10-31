package com.movsoftware.blockhouse.route_tracker.service_implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.movsoftware.blockhouse.route_tracker.entities.Wall;
import com.movsoftware.blockhouse.route_tracker.repository.WallRepository;
import com.movsoftware.blockhouse.route_tracker.service.WallService;

@Service
public class WallServiceImplementation implements WallService {

    @Autowired
    private WallRepository wallRepository;

    @Override
    public Wall saveWall(Wall wall) {
        return wallRepository.save(wall);
    }

    @Override
    public Optional<Wall> findWallById(String id) {
        return wallRepository.findById(id);
    }

    @Override
    public List<Wall> findAllWalls() {
        return (List<Wall>) wallRepository.findAll();
    }

    @Override
    public List<Wall> findWallsByGymId(String gymId) {
        return wallRepository.findAllByGymId(gymId);
    }

    @Override
    public List<Wall> findAllActiveWalls(String gymId) {
        return wallRepository.findAllByGymIdAndIsActive(gymId, true);
    }

    @Override
    public void deleteWallById(String id) {
        wallRepository.deleteById(id);
    }
}

