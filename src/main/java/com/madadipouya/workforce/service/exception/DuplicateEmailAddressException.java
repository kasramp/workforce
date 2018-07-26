package com.madadipouya.workforce.service.exception;

public class DuplicateEmailAddressException extends Exception {

    private final String field;

    private final String message;

    public DuplicateEmailAddressException(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public String getField() {
        return field;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
