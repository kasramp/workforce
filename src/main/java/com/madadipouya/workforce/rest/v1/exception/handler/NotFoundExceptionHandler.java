package com.madadipouya.workforce.rest.v1.exception.handler;

import com.madadipouya.workforce.rest.v1.response.impl.NotFoundResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class NotFoundExceptionHandler implements RestExceptionHandler<NotFoundResponse> {

    private static final Logger logger = LoggerFactory.getLogger(NotFoundExceptionHandler.class);

    @ExceptionHandler(value = {EntityNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    @Override
    public NotFoundResponse handle(Exception ex) {
        logger.info("Failed to find the entity", ex);
        return new NotFoundResponse(ex.getMessage());
    }
}
