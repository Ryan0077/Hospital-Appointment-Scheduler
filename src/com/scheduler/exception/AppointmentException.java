package com.scheduler.exception;

/**
 * A custom exception for handling scheduling errors.
 * Demonstrates Module 3.1.
 */
public class AppointmentException extends Exception {

    public AppointmentException(String message) {
        super(message);
    }
}