package com.movsoftware.blockhouse.route_tracker.pojo;

public enum SortBy {
    HARDEST("Hardest"),
    EASIEST("Easiest"),
    RATING("Rating"),
    MOST_REPEATED("Most Repeated"),
    LEAST_REPEATED("Least Repeated");

    private final String displayName;

    SortBy(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
