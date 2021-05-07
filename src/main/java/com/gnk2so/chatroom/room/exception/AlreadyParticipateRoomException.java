package com.gnk2so.chatroom.room.exception;

public class AlreadyParticipateRoomException extends RuntimeException {
 
    public static final String MESSAGE = "User already participates in the room";

    public AlreadyParticipateRoomException() {
        super(MESSAGE);
    }

}
