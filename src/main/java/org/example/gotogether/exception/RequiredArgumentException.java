package org.example.gotogether.exception;

public class RequiredArgumentException extends RuntimeException {
    public RequiredArgumentException(String message) {
        super(message);
    }
}
