package com.gnk2so.chatroom.room.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import com.gnk2so.chatroom.room.exception.FullRoomException;
import com.gnk2so.chatroom.room.exception.AlreadyParticipateRoomException;
import com.gnk2so.chatroom.user.mock.UserMock;
import com.gnk2so.chatroom.user.model.User;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RoomTest {

    @Test
    public void shouldHasTypePublicWhenCreateNewPublicRoom() {
        Room room = Room.publicRoom("Public");
        assertTrue(room.isPublic());
    }

    @Test
    public void shouldHasTypePrivateWhenCreateNewPrivateRoom() {
        Room room = Room.privateRoom("Private", "P@ssw0rd");
        assertTrue(room.isPrivate());
    }
    
    @Test
    public void shouldInitialWithZeroParticipantsWhenCreateNewPublicRoom() {
        Room room = Room.publicRoom("Public");
        assertEquals(0, room.getParticipants().size());
    }

    @Test
    public void shouldInitialWithZeroParticipantsWhenCreateNewPrivateRoom() {
        Room room = Room.privateRoom("Private", "P@ssw0rd");
        assertEquals(0, room.getParticipants().size());
    }

    @Test
    public void shouldIncrementParticipantsWhenAddNewParticipants() {
        User user = UserMock.build();
        
        Room room = Room.publicRoom("Public");
        room.add(user);
        
        assertEquals(1, room.getParticipants().size());
    }

    @Test
    public void shouldDecrementParticipantsWhenRemoveNewParticipants() {
        int size = 5;
        List<User> participants = UserMock.buildList(size);
        
        Room room = Room.publicRoom("Public");
        room.setParticipants(participants);
        room.remove(room.getParticipants().get(0));

        assertEquals(size - 1, room.getParticipants().size());
    }

    @Test
    public void shouldThrowFullRoomExceptionWhenAddNewParticipantInFullRoom() {
        User user = UserMock.build();
        List<User> participants = UserMock.buildList(Room.LIMIT_PARTICIPANTS);

        Room room = Room.publicRoom("Public");
        room.setParticipants(participants);

        assertThrows(FullRoomException.class, () -> {
            room.add(user);
        });
    }

    @Test
    public void shouldThrowAlreadyParticipateRoomExceptionWhenAddTheSameUserMoreThanOneTime() {
        User user = UserMock.build();
        
        Room room = Room.publicRoom("Public");
        room.add(user);

        assertThrows(AlreadyParticipateRoomException.class, () -> {
            room.add(user);
        });
    }

}
