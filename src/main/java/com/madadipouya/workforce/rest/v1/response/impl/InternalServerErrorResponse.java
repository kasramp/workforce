package com.madadipouya.workforce.rest.v1.response.impl;

import com.madadipouya.workforce.rest.v1.response.RestResponse;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

public class InternalServerErrorResponse extends AbstractRestResponse implements RestResponse {

    public InternalServerErrorResponse(String message) {
        super(INTERNAL_SERVER_ERROR.value(), message);
    }
}