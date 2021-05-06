package com.gnk2so.chatroom.room.service;

import com.gnk2so.chatroom.room.exception.RoomNotFoundException;
import com.gnk2so.chatroom.room.model.Room;
import com.gnk2so.chatroom.room.repository.RoomRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomRepository repository;

    @Override
    public Room save(Room room) {
        return repository.save(room);
    }

    @Override
    public Room findByChannel(String channel) {
        return repository.findByChannel(channel)
            .orElseThrow(RoomNotFoundException::new);
    }
    
}
