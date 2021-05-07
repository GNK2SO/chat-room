package com.gnk2so.chatroom.room.exception;

public class DontParticipateRoomException extends RuntimeException {
 
    public static final String MESSAGE = "User don't participates in the room";

    public DontParticipateRoomException() {
        super(MESSAGE);
    }

}