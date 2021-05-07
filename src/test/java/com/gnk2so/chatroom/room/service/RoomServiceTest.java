package com.gnk2so.chatroom.room.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.gnk2so.chatroom.room.exception.RoomNotFoundException;
import com.gnk2so.chatroom.room.mock.RoomMock;
import com.gnk2so.chatroom.room.model.Room;
import com.gnk2so.chatroom.room.repository.RoomRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class RoomServiceTest {

    @Autowired
    private RoomService service;

    @MockBean
    private RoomRepository repository;
    
    @Test
    public void shouldReturnRoomWithIdWhenSaveRoomSuccessfully() {
        Room room = Room.publicRoom("Public");
        when(repository.save(any(Room.class))).thenReturn(RoomMock.withId(room));
        
        Room savedRoom = service.save(room);
        
        assertNotNull(savedRoom.getId());
    }

    @Test
    public void shouldReturnRoomWithSameDataWhenSaveRoomSuccessfully() {
        Room room = Room.publicRoom("Public");
        when(repository.save(any(Room.class))).thenReturn(RoomMock.withId(room));
        
        Room savedRoom = service.save(room);

        assertEquals(room.getTitle(), savedRoom.getTitle());
        assertEquals(room.getPassword(), savedRoom.getPassword());
        assertEquals(room.getChannel(), savedRoom.getChannel());
        assertEquals(room.getType(), savedRoom.getType());
        assertEquals(room.getParticipants(), savedRoom.getParticipants());
    }
    
    @Test
    public void shouldReturnRoomWhenFindRoomByChannelSuccessfully() {
        Room room = Room.publicRoom("Public");
        when(repository.findByChannel(anyString())).thenReturn(Optional.of(room));

        Room gettedRoom = service.findByChannel("CHANNEL");

        assertEquals(room, gettedRoom);
    }

    @Test
    public void shouldThrowRoomNotFoundExceptionWhenFindRoomByChannelSuccessfully() {
        when(repository.findByChannel(anyString())).thenReturn(Optional.empty());

        assertThrows(RoomNotFoundException.class, () -> {
            service.findByChannel("CHANNEL");
        });
    }


    @Test
    public void shouldReturnRoomWhenFindRoomByIdSuccessfully() {
        Room room = Room.publicRoom("Public");
        when(repository.findById(anyLong())).thenReturn(Optional.of(room));

        Room gettedRoom = service.findById(1L);

        assertEquals(room, gettedRoom);
    }

    @Test
    public void shouldThrowRoomNotFoundExceptionWhenFindRoomByIdSuccessfully() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RoomNotFoundException.class, () -> {
            service.findById(1L);
        });
    }
}
