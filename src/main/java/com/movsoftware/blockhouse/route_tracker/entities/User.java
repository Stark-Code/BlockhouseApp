package com.movsoftware.blockhouse.route_tracker.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.movsoftware.blockhouse.route_tracker.pojo.SortBy;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "gym_user")
public class User {

    // ---------- Primary Key and References --------------

    @Id
    // @GeneratedValue(generator = "UUID")
    // @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "uid", updatable = false, nullable = false)
    private String uid;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "gym_id", nullable = false)
    @JsonBackReference
    private Gym gym;

    // --------------- Basic Info Fields ------------------

    @Column(name = "username", nullable = true)
    private String username;

    @Column(name = "name", nullable = true)
    private String name;

    @Column(name = "first_name", nullable = true)
    private String firstName;

    @Column(name = "last_name", nullable = true)
    private String lastName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "points", nullable = false)
    private Integer points = 0;

    @ElementCollection(fetch = FetchType.EAGER) // Eager fetch to ensure permissions are always loaded with the User
    @Column(name = "permissions")
    private Set<String> permissions; // This will store the permissions

    // Add fields for saving sorting and filter settings
    @Enumerated(EnumType.STRING)
    @Column(name = "sort_by")
    private SortBy sortBy;

    // --------------- Status Fields ----------------------

    @Column(name = "is_active")
    private Boolean isActive = true;

    // --------------- Relationships -----------------------

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Log> logbook = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<ChallengeLog> challengeLogs;

    // ------------------ Getters and Setters -------------------

    public String getUid() {
        return uid;
    }

    public Gym getGym() {
        return gym;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Integer getPoints() {
        return points;
    }

    public SortBy getSortBy() {
        return sortBy;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGym(Gym gym) {
        this.gym = gym;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void incrementPoints(Integer points) {
        this.points += points;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public List<Log> getLogbook() {
        return logbook;
    }

    public List<ChallengeLog> getChallengesLog() {
        return challengeLogs;
    }

    public void addLog(Log log) {
        log.setUser(this); // Assuming that you have a setUser method in Log class
        this.logbook.add(log);
    }

    public void addChallengeLog(ChallengeLog challengeLog) {
        // challenge.setUser(this); // Assuming that you have a setUser method in Challenge class
        this.challengeLogs.add(challengeLog);
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }

    public void setSortBy(SortBy sortBy) {
        this.sortBy = sortBy;
    }

    // Other methods like equals(), hashCode(), and toString() can go here
}
