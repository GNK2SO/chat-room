package com.gnk2so.chatroom.room.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.gnk2so.chatroom.room.controlller.filter.PageRoomFilter;
import com.gnk2so.chatroom.room.exception.RoomNotFoundException;
import com.gnk2so.chatroom.room.mock.RoomMock;
import com.gnk2so.chatroom.room.model.Room;
import com.gnk2so.chatroom.room.repository.RoomRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

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
    public void shouldReturnEmptyPageRoomWhenNotFindRoom() {
        Page<Room> pageRooms = new PageImpl<>(RoomMock.buildList(0));
        when(repository.findAll(any(Pageable.class))).thenReturn(pageRooms);

        Page<Room> gettedPage = service.findAll(new PageRoomFilter());

        assertEquals(pageRooms, gettedPage);
        assertTrue(gettedPage.isEmpty());
    }

    @Test
    public void shouldReturnFilledPageRoomWhenFindRoomsSuccessfully() {
        Page<Room> pageRooms = new PageImpl<>(RoomMock.buildList(5));
        when(repository.findAll(any(Pageable.class))).thenReturn(pageRooms);

        Page<Room> gettedPage = service.findAll(new PageRoomFilter());

        assertEquals(pageRooms, gettedPage);
        assertEquals(5, gettedPage.getNumberOfElements());
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
