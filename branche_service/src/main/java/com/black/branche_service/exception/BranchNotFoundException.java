package com.black.branche_service.exception;

public class BranchNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public BranchNotFoundException(String message) {
        super(message);
    }
}
