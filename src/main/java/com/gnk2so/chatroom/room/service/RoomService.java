package com.gnk2so.chatroom.room.service;

import com.gnk2so.chatroom.room.controlller.filter.PageRoomFilter;
import com.gnk2so.chatroom.room.model.Room;

import org.springframework.data.domain.Page;

public interface RoomService {
    Room save(Room room);
    Room findByChannel(String channel);
    Room findById(long id);
    Page<Room> findAll(PageRoomFilter filter);
}
