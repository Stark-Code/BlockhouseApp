package com.movsoftware.blockhouse.route_tracker.entities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.hibernate.annotations.GenericGenerator;

import com.movsoftware.blockhouse.route_tracker.pojo.NumberOfTries;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Log {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(name = "sent_date")
    private LocalDateTime sentDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "number_of_tries")
    private NumberOfTries numberOfTries;

    // Logbook will show users rating of route
    @Column(name = "rating")
    private int rating;

    @ManyToOne
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Getters and setters

    public Log(String numberOfTriesStr, int rating, Route route, User user) {
        this.sentDate = LocalDateTime.now();
        this.numberOfTries = NumberOfTries.valueOf(numberOfTriesStr);
        this.rating = rating;
        this.route = route;
        this.user = user;
    }

    public Log() {}

    public String getId() { return id; }
    public LocalDateTime getSentDate() { return sentDate; }
    public void setSentDate(LocalDateTime sentDate) { this.sentDate = sentDate; }
    public NumberOfTries getNumberOfTries() { return numberOfTries; }
    public void setNumberOfTries(NumberOfTries numberOfTries) { this.numberOfTries = numberOfTries; }
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    public Route getRoute() { return route; }
    public void setRoute(Route route) { this.route = route; }
    public void setUser(User user) { this.user = user; }

    // Methods
    public String getFormattedSentDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");
        return sentDate.format(formatter);
    }

}

