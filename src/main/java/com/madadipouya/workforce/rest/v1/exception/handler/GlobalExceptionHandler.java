package com.madadipouya.workforce.rest.v1.exception.handler;

import com.madadipouya.workforce.rest.v1.response.impl.InternalServerErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler implements RestExceptionHandler<InternalServerErrorResponse> {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    @Override
    public InternalServerErrorResponse handle(Exception ex) {
        logger.warn("An unexpected exception occurred. Something went wrong", ex);
        return new InternalServerErrorResponse("Unable to process your request at the moment");
    }
}
