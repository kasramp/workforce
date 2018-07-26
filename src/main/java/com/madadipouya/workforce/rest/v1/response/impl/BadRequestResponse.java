package com.madadipouya.workforce.rest.v1.response.impl;

import com.madadipouya.workforce.rest.v1.response.RestResponse;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class BadRequestResponse extends AbstractRestResponse implements RestResponse {

    private List<FieldError> fieldErrors;

    public BadRequestResponse(String message) {
        super(BAD_REQUEST.value(), message);
        this.fieldErrors = new ArrayList<>();
    }

    public void addFieldError(String field, String message) {
        fieldErrors.add(new FieldError(field, message));
    }

    public List<FieldError> getFieldErrors() {
        return fieldErrors;
    }

    private static class FieldError {

        private final String field;

        private final String error;

        public FieldError(String field, String error) {
            this.field = field;
            this.error = error;
        }

        public String getField() {
            return field;
        }

        public String getError() {
            return error;
        }
    }
}
