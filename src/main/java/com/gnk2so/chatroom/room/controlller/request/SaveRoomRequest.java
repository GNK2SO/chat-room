package com.gnk2so.chatroom.room.controlller.request;

import static org.apache.logging.log4j.util.Strings.isBlank;

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
public class SaveRoomRequest {

    @NotNull
    @Size(min = 6, max = 24)
    private String title;

    @Size(max = 16, message = "size must be lower than 16")
    private String password;

    public Room getRoom(User user) {
        Room room = getRoom();
        room.add(user);
        return room;
    }

    private Room getRoom() {
        if(isBlank(password)) {
            return Room.publicRoom(title);
        } 
        return Room.privateRoom(title, encryptedPassword());
    }
    
    public String encryptedPassword() {
        return new BCryptPasswordEncoder().encode(password);
    }
}
