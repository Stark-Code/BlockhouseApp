package com.movsoftware.blockhouse.route_tracker.service;

import java.util.List;
import java.util.Optional;

import com.movsoftware.blockhouse.route_tracker.entities.Wall;

public interface WallService {
    Wall saveWall(Wall wall);
    Optional<Wall> findWallById(String id);
    List<Wall> findAllWalls();
    List<Wall> findAllActiveWalls(String gymId);
    List<Wall> findWallsByGymId(String gymId);
    void deleteWallById(String id);
}
