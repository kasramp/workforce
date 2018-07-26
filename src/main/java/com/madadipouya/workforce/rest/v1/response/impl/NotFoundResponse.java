package com.madadipouya.workforce.rest.v1.response.impl;

import com.madadipouya.workforce.rest.v1.response.RestResponse;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public class NotFoundResponse extends AbstractRestResponse implements RestResponse {

    public NotFoundResponse(String message) {
        super(NOT_FOUND.value(), message);
    }
}
