package com.gnk2so.chatroom.room.mock;

import com.gnk2so.chatroom.room.controlller.request.SaveRoomRequest;

public class SaveRoomRequestMock {

    public static SaveRoomRequest build() {
        return new SaveRoomRequest("Dev Room", null);
    } 

    public static SaveRoomRequest build(String title) {
        return new SaveRoomRequest(title, null);
    }

    public static SaveRoomRequest build(String title, String password) {
        return new SaveRoomRequest(title, password);
    }
}
