package com.black.passenger_service.exception;

public class PassengerNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public PassengerNotFoundException(String message) {
        super(message);
    }
}
