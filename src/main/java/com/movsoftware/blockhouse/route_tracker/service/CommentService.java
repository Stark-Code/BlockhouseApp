package com.movsoftware.blockhouse.route_tracker.service;

import java.util.List;
import java.util.Optional;

import com.movsoftware.blockhouse.route_tracker.entities.Comment;

public interface CommentService {
    List<Comment> getAllComments();

    Optional<Comment> getCommentById(String id);

    Comment saveComment(Comment comment);

    void deleteComment(String id);
}
