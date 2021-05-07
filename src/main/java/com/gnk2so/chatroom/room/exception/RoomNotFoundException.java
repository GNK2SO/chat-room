package com.gnk2so.chatroom.room.exception;

public class RoomNotFoundException extends RuntimeException {
    
    public static final String MESSAGE = "Room not found";

    public RoomNotFoundException() {
        super(MESSAGE);
    }

}
