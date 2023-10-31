package com.movsoftware.blockhouse.route_tracker.exceptions;

public class PermissionsException extends RuntimeException {

    public PermissionsException(String message) {
        super(message);
    }

    public PermissionsException(String message, Throwable cause) {
        super(message, cause);
    }
}
