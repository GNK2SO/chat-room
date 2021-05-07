package com.gnk2so.chatroom.room.mock;

import com.gnk2so.chatroom.room.controlller.request.JoinRoomRequest;

public class JoinRoomRequestMock {
    public static JoinRoomRequest build() {
        return new JoinRoomRequest();
    } 

    public static JoinRoomRequest build(String password) {
        return new JoinRoomRequest(password);
    }
}
