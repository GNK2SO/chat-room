package com.gnk2so.chatroom.room.exception;

public class InvalidRoomPasswordException extends RuntimeException {
 
    public static final String MESSAGE = "Password is incorrect";

    public InvalidRoomPasswordException() {
        super(MESSAGE);
    }

}