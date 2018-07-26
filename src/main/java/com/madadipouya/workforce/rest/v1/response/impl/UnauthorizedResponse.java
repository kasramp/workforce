package com.madadipouya.workforce.rest.v1.response.impl;

import com.madadipouya.workforce.rest.v1.response.RestResponse;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class UnauthorizedResponse extends AbstractRestResponse implements RestResponse {

    public UnauthorizedResponse(String message) {
        super(UNAUTHORIZED.value(), message);
    }
}
