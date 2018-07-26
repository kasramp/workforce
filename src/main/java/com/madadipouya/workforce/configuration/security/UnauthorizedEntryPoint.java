package com.madadipouya.workforce.configuration.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.madadipouya.workforce.rest.v1.response.impl.UnauthorizedResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UnauthorizedEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        new ObjectMapper().writeValue(response.getOutputStream(), new UnauthorizedResponse("You must authenticate first"));
    }
}