package com.movsoftware.blockhouse.route_tracker.entities;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Wall {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "gym_id", nullable = false)
    @JsonBackReference  // non-owning side
    private Gym gym;

    @OneToMany(mappedBy = "wall")  // assuming Route has a 'wall' field linking back to Wall
    @JsonManagedReference  // owning side
    private List<Route> routes;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "set_on_date")
    private LocalDate setOnDate;

    public Wall(String name) {
        this.name = name;
        this.isActive = true;
        this.setOnDate = LocalDate.now();  // set the date to current date
    }

    public Wall() {}

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Gym getGym() { return gym; }
    public void setGym(Gym gym) { this.gym = gym; }

    public List<Route> getRoutes() { return routes; }
    public void setRoutes(List<Route> routes) { this.routes = routes; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public LocalDate getSetOnDate() {
        return setOnDate;
    }

    public void setSetOnDate(LocalDate setOnDate) {
        this.setOnDate = setOnDate;
    }

    public String getFormattedSetOnDate() {
        return setOnDate.toString();  // LocalDate's default format is yyyy-MM-dd
    }
}
