package com.movsoftware.blockhouse.route_tracker.dto;

import java.util.Set;

public class SyncUserRequest {

    private String uid;
    private String email;
    private String name;
    private String gymId;
    private Set<String> permissions;

    public SyncUserRequest() {
    }

    public SyncUserRequest(String uid, String email, String name, String gymId, Set<String> permissions) {
        this.uid = uid;
        this.email = email;
        this.name = name;
        this.gymId = gymId;
        this.permissions = permissions;
    }

    public String getUid() {
        return this.uid;
    }

    public String getEmail() {
        return this.email;
    }

    public String getName() {
        return this.name;
    }

    public String getGymId() {
        return this.gymId;
    }

    public Set<String> getPermissions() {
        return this.permissions;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGymId(String gymId) {
        this.gymId = gymId;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }

    // Methods
    @Override
    public String toString() {
        return "SyncUserRequest{" +
                "uid='" + uid + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", gymId='" + gymId + '\'' +
                ", permissions=" + permissions +
                '}';
    }

}
