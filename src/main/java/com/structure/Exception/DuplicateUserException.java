package com.structure.Exception;

public class DuplicateUserException extends Exception {
    private String field;

    public DuplicateUserException(String field, String message) {
        super(message);
        this.field = field;
    }

    public String getField() {
        return field;
    }
}
