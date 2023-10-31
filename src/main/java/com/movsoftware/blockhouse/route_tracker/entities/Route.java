package com.movsoftware.blockhouse.route_tracker.entities;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.movsoftware.blockhouse.route_tracker.pojo.BoulderingStyle;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Route {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "setter_name")
    private String setterName;

    @Column(name = "first_ascent")
    private String firstAscent;

    @Column(name = "grade")
    private String grade;

    // Overall rating of route
    @Column(name = "rating")
    private Double rating;

    @Column(name = "total_sends")
    private Integer totalSends;

    @Column(name = "description", length = 1000)
    private String description;

    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL)
    @JsonManagedReference  // owning side
    private List<Comment> comments;

    @Column(name = "is_active")
    private Boolean isActive;

    @Enumerated(EnumType.STRING)
    private BoulderingStyle style;

    @ManyToOne
    @JoinColumn(name = "wall_id", nullable = false)
    @JsonBackReference // non-owning side
    private Wall wall;

    public Route(String name, String setterName, String grade, BoulderingStyle style, String description) {
        this.name = name;
        this.setterName = setterName;
        this.firstAscent = "N/A";
        this.grade = grade;
        this.rating = 0.0;
        this.totalSends = 0;
        this.style = style;
        this.description = description;
        this.comments = new ArrayList<Comment>();
        this.isActive = true;
    }

    public Route() {
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSetterName() {
        return setterName;
    }

    public void setSetterName(String setterName) {
        this.setterName = setterName;
    }

    public String getFirstAscent() {
        return firstAscent;
    }

    public void setFirstAscent(String firstAscent) {
        this.firstAscent = firstAscent;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Integer getTotalSends() {
        return totalSends;
    }

    public void setTotalSends(Integer totalSends) {
        this.totalSends = totalSends;
    }

    public BoulderingStyle getStyle() {
        return style;
    }

    public void setStyle(BoulderingStyle style) {
        this.style = style;
    }

    public Wall getWall() {
        return wall;
    }

    public void setWall(Wall wall) {
        this.wall = wall;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
    this.comments = comments;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    // Methods

    // Add a comment to the route
    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    public void incrementTotalSends() {
        this.totalSends++;
    }
}
