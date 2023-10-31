package com.movsoftware.blockhouse.route_tracker.pojo;

public enum BoulderingStyle {
    CRIMPY("Crimpy"),
    SLOPEY("Slopey"),
    PINCHY("Pinchy"),
    JUGGY("Juggy"),
    DYNAMIC("Dynamic"),
    STATIC("Static"),
    TECHNICAL("Technical"),
    POWERFUL("Powerful"),
    BALANCY("Balancy"),
    SLAB("Slab");

    private final String displayName;

    BoulderingStyle(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

