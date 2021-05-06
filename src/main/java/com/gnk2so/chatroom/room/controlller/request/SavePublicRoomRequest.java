package com.gnk2so.chatroom.room.controlller.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.gnk2so.chatroom.room.model.Room;
import com.gnk2so.chatroom.user.model.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SavePublicRoomRequest {
    
    @NotNull
    @Size(min = 6, max = 24)
    private String title;

    public Room getRoom(User user) {
        Room room = Room.publicRoom(title);
        room.add(user);
        return room;
    }
}
