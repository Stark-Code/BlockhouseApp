package com.movsoftware.blockhouse.route_tracker.pojo;

public enum NumberOfTries {
    FLASH("Flash", 1.0f),
    TWO_ATTEMPTS("2 Attempts", 0.9f),
    THREE_ATTEMPTS("3 Attempts", 0.8f),
    MORE_THAN_THREE_ATTEMPTS("More than 3 attempts", 0.7f);

    private final String displayName;
    private final float multiplier;

    NumberOfTries(String displayName, float multiplier) {
        this.displayName = displayName;
        this.multiplier = multiplier;
    }
    
    public String getDisplayName() {
        return displayName;
    }

    public float getMultiplier() {
        return multiplier;
    }
}

