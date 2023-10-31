package com.movsoftware.blockhouse.route_tracker.entities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Comment {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(name = "comment_text", nullable = false)
    private String commentText;

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name= "author" , nullable = false)
    private String author;

    @ManyToOne
    @JoinColumn(name = "route_id", nullable = false)
    @JsonBackReference  // non-owning side
    private Route route;

    // Constructors, getters, and setters
    public Comment(String commentText, LocalDateTime createdAt, Route route, String author) {
        this.commentText = commentText;
        this.createdAt = createdAt;
        this.route = route;
        this.author = author;
    }

    public Comment() {
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getCommentText() {
        return commentText;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Route getRoute() {
        return route;
    }

    public String getAuthor() {
        return author;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getFormattedCreatedAt() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");
        return createdAt.format(formatter);
    }
}
