package com.structure.Exception;

public class ForeignKeyViolationException extends Exception {
    public ForeignKeyViolationException(String message) {
        super(message);
    }

    public ForeignKeyViolationException(String message, Throwable cause) {
        super(message, cause);
    }
}