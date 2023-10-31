package com.movsoftware.blockhouse.route_tracker.pojo;

public enum Grade {
    
    WHITE("White", 100),
    GREEN("Green", 200),
    BLUE("Blue", 300),
    ORANGE("Orange", 400),
    RED("Red", 500),
    BLACK("Black", 600);

    private final String displayName;
    private final int basePoints;

    Grade(String displayName, int basePoints) {
        this.displayName = displayName;
        this.basePoints = basePoints;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getBasePoints() {
        return basePoints;
    }

    public int calculatePoints(int tries) {
        if (tries == 1) {
            return getBasePoints();
        } else if (tries == 2) {
            return (int) (getBasePoints() * 0.9);
        } else if (tries == 3) {
            return (int) (getBasePoints() * 0.85);
        } else {
            return (int) (getBasePoints() * 0.8);
        }
    }
}

