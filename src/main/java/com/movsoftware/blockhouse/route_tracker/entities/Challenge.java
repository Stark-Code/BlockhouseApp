package com.movsoftware.blockhouse.route_tracker.entities;

import java.util.List;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Challenge {
    
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private String id;
    
    @ManyToOne
    @JoinColumn(name = "gym_id", nullable = false)
    @JsonBackReference  // non-owning side
    private Gym gym;

    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "is_ongoing")
    private Boolean isOngoing;

    @Column(name = "is_periodic")
    private Boolean isPeriodic;

    @OneToMany(mappedBy = "challenge")
    @JsonBackReference
    private List<ChallengeLog> challengesLog;    

    public Challenge() {}

    public Challenge(String name, String description, Boolean isOngoing, Boolean isPeriodic) {
        this.name = name;
        this.description = description;
        this.isOngoing = isOngoing;
        this.isPeriodic = isPeriodic;
    }

    public String getId() { return id; }
    public Gym getGym() { return gym; }
    public void setGym(Gym gym) { this.gym = gym; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Boolean getIsOngoing() { return isOngoing; }
    public void setIsOngoing(Boolean isOngoing) { this.isOngoing = isOngoing; }
    public Boolean getIsPeriodic() { return isPeriodic; }
    public void setIsPeriodic(Boolean isPeriodic) { this.isPeriodic = isPeriodic; }
    public List<ChallengeLog> getChallengesLog() { return challengesLog; }
    public void setChallengesLog(List<ChallengeLog> challengesLog) { this.challengesLog = challengesLog; }


}
