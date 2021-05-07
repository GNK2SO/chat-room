package com.gnk2so.chatroom.room.exception;

public class FullRoomException extends RuntimeException {
    
    public static final String MESSAGE = "the room is full";

    public FullRoomException() {
        super(MESSAGE);
    }
    
}
