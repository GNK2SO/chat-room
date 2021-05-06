package com.gnk2so.chatroom.config.security;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gnk2so.chatroom.config.web.ErrorResponse;

import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

public class CustomAuthEntryPoint implements AuthenticationEntryPoint {

    public static final String MESSAGE = "Invalid/Expired token";

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        ErrorResponse errorResponse = new ErrorResponse(UNAUTHORIZED.value(), MESSAGE);
        ObjectMapper serializer = new Jackson2JsonEncoder().getObjectMapper();
        String responseJSON = serializer.writeValueAsString(errorResponse);
        response.setStatus(UNAUTHORIZED.value());
        response.getWriter().write(responseJSON);
    }
}
