package com.gnk2so.chatroom.room.service;

import com.gnk2so.chatroom.room.exception.RoomNotFoundException;
import com.gnk2so.chatroom.room.model.Room;
import com.gnk2so.chatroom.room.repository.RoomRepository;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository repository;

    @Override
    public Room save(Room room) {
        return repository.save(room);
    }

    @Override
    public Room findByChannel(String channel) {
        return repository.findByChannel(channel)
            .orElseThrow(RoomNotFoundException::new);
    }

    @Override
    public Room findById(long id) {
        return repository.findById(id)
            .orElseThrow(RoomNotFoundException::new);
    }
    
}
