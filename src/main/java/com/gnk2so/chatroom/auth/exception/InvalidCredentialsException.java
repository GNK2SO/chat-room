package com.gnk2so.chatroom.auth.exception;

public class InvalidCredentialsException extends RuntimeException {
    
    public static final String MESSAGE = "Invalid credentials!";
    
    public InvalidCredentialsException() {
        super(MESSAGE);
    }

}
