package com.gnk2so.chatroom.config.security;

import static org.springframework.http.HttpStatus.FORBIDDEN;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gnk2so.chatroom.config.web.ErrorResponse;

import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

public class CustomAccessHandler implements AccessDeniedHandler {

    public static final String MESSAGE = "Don't have permission";

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {
        
        ErrorResponse errorResponse = new ErrorResponse(FORBIDDEN.value(), MESSAGE);
        ObjectMapper serializer = new Jackson2JsonEncoder().getObjectMapper();
        String responseJSON = serializer.writeValueAsString(errorResponse);
        response.setStatus(FORBIDDEN.value());
        response.getWriter().write(responseJSON);
        
    }
    
}
