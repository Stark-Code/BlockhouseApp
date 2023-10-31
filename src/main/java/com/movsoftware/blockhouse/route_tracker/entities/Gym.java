package com.movsoftware.blockhouse.route_tracker.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Gym {

    // ---------- Primary Key --------------

    @Id
    // @GeneratedValue(generator = "UUID")
    // @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    // --------------- Basic Info Fields ------------------

    @Column(name = "name", nullable = false)
    private String name;

    // --------------- Status Fields ----------------------

    @Column(name = "is_active")
    private Boolean isActive = true;

    // --------------- Relationships -----------------------

    @OneToMany(mappedBy = "gym")
    @JsonManagedReference
    private List<Wall> walls = new ArrayList<>();

    @OneToMany(mappedBy = "gym")
    @JsonManagedReference
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "gym")
    @JsonManagedReference
    private List<Challenge> challenges = new ArrayList<>();

    // Constructors
    public Gym(String name) {
        // this.id = UUID.randomUUID().toString();
        this.id = "1";
        this.name = name;
        this.walls = new ArrayList<>();
        this.users = new ArrayList<>();
    }

    // Default constructor for JPA
    public Gym() {
        // This is the default constructor
    }

    // ------------------ Getters and Setters -------------------

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

    public List<Wall> getWalls() {
        return walls;
    }

    public void setWalls(List<Wall> walls) {
        this.walls = walls;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    // Other methods like equals(), hashCode(), and toString() can go here
}
