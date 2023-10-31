package com.movsoftware.blockhouse.route_tracker.service_implementation;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.movsoftware.blockhouse.route_tracker.entities.Comment;
import com.movsoftware.blockhouse.route_tracker.repository.CommentRepository;
import com.movsoftware.blockhouse.route_tracker.service.CommentService;

@Service
public class CommentServiceImplementation implements CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Override
    public List<Comment> getAllComments() {
    Iterable<Comment> comments = commentRepository.findAll();
    List<Comment> commentList = StreamSupport
        .stream(comments.spliterator(), false)
        .collect(Collectors.toList());
    return commentList;
}

    @Override
    public Optional<Comment> getCommentById(String id) {
        return commentRepository.findById(id);
    }

    @Override
    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public void deleteComment(String id) {
        commentRepository.deleteById(id);
    }
}

