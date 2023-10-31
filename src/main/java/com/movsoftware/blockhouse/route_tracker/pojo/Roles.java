package com.movsoftware.blockhouse.route_tracker.pojo;

public enum Roles {
    VIEWER("Allows general access"),
    ADMIN("Allows access to administrative functions.");

    private String description;

    Roles(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}