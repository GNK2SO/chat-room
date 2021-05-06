package com.gnk2so.chatroom.room.mock;

import java.util.Random;

import com.gnk2so.chatroom.room.model.Room;

public class RoomMock {

    public static Room withId(Room room) {
        return Room.builder()
            .id(new Random().nextLong())
            .title(room.getTitle())
            .password(room.getPassword())
            .type(room.getType())
            .channel(room.getChannel())
            .participants(room.getParticipants())
            .build();
    }
    
}
