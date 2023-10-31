package com.movsoftware.blockhouse.route_tracker.exceptions;

public class GymNotFound extends RuntimeException {

    public GymNotFound(String message) {
        super(message);
    }

    public GymNotFound(String message, Throwable cause) {
        super(message, cause);
    }
}