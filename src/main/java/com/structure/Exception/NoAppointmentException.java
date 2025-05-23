package com.structure.Exception;

public class NoAppointmentException extends RuntimeException {
    public NoAppointmentException(String message) {
        super(message);
    }
}
