package com.gnk2so.chatroom.config.web;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;

import java.util.List;
import java.util.stream.Collectors;

import com.gnk2so.chatroom.auth.exception.InvalidCredentialsException;
import com.gnk2so.chatroom.user.exception.AlreadyUsedEmailException;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class WebExceptionHandler {
    
    @ExceptionHandler(value = {
        MethodArgumentNotValidException.class
    })
    public ResponseEntity<ErrorResponse> badRequest(MethodArgumentNotValidException exception) {

		List<FieldError> fieldsErrors = exception.getBindingResult().getFieldErrors();

        List<ValidationError> validationErrors = fieldsErrors.stream().map(error -> {
            return new ValidationError(error.getField(), error.getDefaultMessage());
		}).collect(Collectors.toList());
		
        ErrorResponse response = new ErrorResponse(BAD_REQUEST.value(), null, validationErrors);

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = {
        InvalidCredentialsException.class
    })
    public ResponseEntity<ErrorResponse> unauthorized(RuntimeException exception) {
        ErrorResponse response = new ErrorResponse(UNAUTHORIZED.value(), exception.getMessage());
        return ResponseEntity.status(UNAUTHORIZED.value()).body(response);
    }

    @ExceptionHandler(value = {
        AlreadyUsedEmailException.class
    })
    public ResponseEntity<ErrorResponse> conflict(RuntimeException exception) {
        ErrorResponse response = new ErrorResponse(CONFLICT.value(), exception.getMessage());
        return ResponseEntity.status(CONFLICT.value()).body(response);
    }
}