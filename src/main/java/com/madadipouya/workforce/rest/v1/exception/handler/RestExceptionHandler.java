package com.madadipouya.workforce.rest.v1.exception.handler;

import com.madadipouya.workforce.rest.v1.response.RestResponse;

/**
 * A generic handler to handle different exceptions happen in the system
 * */
public interface RestExceptionHandler<T extends RestResponse> {

    /**
     * Handles an exception and produces a response {@link RestResponse}
     * @param ex any exception
     * @return {@link RestResponse} a response payload
     * */
    T handle(Exception ex);
}
