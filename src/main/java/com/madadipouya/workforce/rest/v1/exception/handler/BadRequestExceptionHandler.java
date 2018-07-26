package com.madadipouya.workforce.rest.v1.exception.handler;

import com.madadipouya.workforce.rest.v1.response.impl.BadRequestResponse;
import com.madadipouya.workforce.service.exception.DuplicateEmailAddressException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class BadRequestExceptionHandler implements RestExceptionHandler<BadRequestResponse> {

    private static final Logger logger = LoggerFactory.getLogger(BadRequestExceptionHandler.class);

    private static final String RESPONSE_MESSAGE = "Validation error";

    @ExceptionHandler(value = {MethodArgumentNotValidException.class, DuplicateEmailAddressException.class,
            HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @Override
    public BadRequestResponse handle(Exception ex) {
        logger.info("Unable to process the invalid request", ex);
        if (ex instanceof MethodArgumentNotValidException) {
            return createResponse(((MethodArgumentNotValidException) ex).getBindingResult().getFieldErrors());
        } else if (ex instanceof DuplicateEmailAddressException) {
            return createResponse((DuplicateEmailAddressException) ex);
        } else {
            return createResponse(ex);
        }
    }

    private BadRequestResponse createResponse(List<org.springframework.validation.FieldError> fieldErrors) {
        BadRequestResponse requestResponse = new BadRequestResponse(RESPONSE_MESSAGE);
        fieldErrors.forEach(error -> requestResponse.addFieldError(error.getField(), error.getDefaultMessage()));
        return requestResponse;
    }

    private BadRequestResponse createResponse(DuplicateEmailAddressException duplicatedEmailAddressException) {
        BadRequestResponse requestResponse = new BadRequestResponse(RESPONSE_MESSAGE);
        requestResponse.addFieldError(duplicatedEmailAddressException.getField(), duplicatedEmailAddressException.getMessage());
        return requestResponse;
    }

    private BadRequestResponse createResponse(Exception exception) {
        logger.info(exception.getMessage());
        BadRequestResponse requestResponse = new BadRequestResponse("Unable to serialize the payload");
        return requestResponse;
    }
}