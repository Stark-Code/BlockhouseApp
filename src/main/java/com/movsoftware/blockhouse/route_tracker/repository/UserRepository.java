package com.movsoftware.blockhouse.route_tracker.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.movsoftware.blockhouse.route_tracker.entities.User;

// _ indicates JPA will traverse the object graph to find the gymId field
public interface UserRepository extends CrudRepository<User, String> {
    Optional<User> findByUidAndGym_IdAndIsActiveIsTrue(String uid, String gymId);
    List<User> findAllByGym_IdAndIsActiveIsTrue(String gymId);
}
