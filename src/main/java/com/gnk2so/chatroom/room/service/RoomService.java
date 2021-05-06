package com.gnk2so.chatroom.room.service;

import com.gnk2so.chatroom.room.model.Room;

public interface RoomService {
    Room save(Room room);
    Room findByChannel(String channel);
}
