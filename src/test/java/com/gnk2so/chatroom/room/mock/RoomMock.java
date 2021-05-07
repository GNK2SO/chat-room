package com.gnk2so.chatroom.room.mock;

import java.util.List;
import java.util.Random;

import com.gnk2so.chatroom.room.model.Room;
import com.gnk2so.chatroom.user.model.User;

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

    public static Room publicRoom(String title, List<User> participants) {
        Room room = Room.publicRoom(title);
        room.setParticipants(participants);
        return room;
    }

    public static Room publicRoom(String title, User firstParticipant) {
        Room room = Room.publicRoom(title);
        room.add(firstParticipant);
        return room;
    }

    public static Room privateRoom(String title, String password, User firstParticipant) {
        Room room = Room.privateRoom(title, password);
        room.add(firstParticipant);
        return room;
    }

    
}
