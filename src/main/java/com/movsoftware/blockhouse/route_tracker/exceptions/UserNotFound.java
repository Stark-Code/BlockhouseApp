package com.movsoftware.blockhouse.route_tracker.exceptions;

public class UserNotFound extends RuntimeException {

    public UserNotFound(String message) {
        super(message);
    }

    public UserNotFound(String message, Throwable cause) {
        super(message, cause);
    }
}
