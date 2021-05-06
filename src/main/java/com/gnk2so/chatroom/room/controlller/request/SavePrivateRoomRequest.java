package com.gnk2so.chatroom.room.controlller.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.gnk2so.chatroom.room.model.Room;
import com.gnk2so.chatroom.user.model.User;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class SavePrivateRoomRequest {

    @NotNull
    @Size(min = 6, max = 24)
    private String title;

    @NotNull
    @Size(min = 6, max = 16)
    private String password;

    public Room getRoom(User user) {
        Room room = Room.privateRoom(title, encryptedPassword());
        room.add(user);
        return room;
    }
    
    public String encryptedPassword() {
        return new BCryptPasswordEncoder().encode(password);
    }
}
