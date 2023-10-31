package com.movsoftware.blockhouse.route_tracker.repository;

import org.springframework.data.repository.CrudRepository;

import com.movsoftware.blockhouse.route_tracker.entities.Comment;

public interface CommentRepository extends CrudRepository<Comment, String>{
    
}


