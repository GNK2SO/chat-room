package com.gnk2so.chatroom.room.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.gnk2so.chatroom.room.exception.FullRoomException;
import com.gnk2so.chatroom.room.exception.AlreadyParticipateRoomException;
import com.gnk2so.chatroom.user.model.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Room {

    public static final int LIMIT_PARTICIPANTS = 10;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false, length = 24)
    private String title;

    @Column(length = 64)
    private String password;

    @Column(nullable = false, length = 64)
    private String channel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomType type;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<User> participants;
    
    public enum RoomType { PUBLIC, PRIVATE }

    public static Room publicRoom(String title) {
        return Room.builder()
            .title(title)
            .type(RoomType.PUBLIC)
            .participants(new ArrayList<>())
            .channel(UUID.randomUUID().toString())
            .build();
    }

    public static Room privateRoom(String title, String password) {
        return Room.builder()
            .title(title)
            .password(password)
            .type(RoomType.PRIVATE)
            .participants(new ArrayList<>())
            .channel(UUID.randomUUID().toString())
            .build();
    }

    public boolean isPublic() {
        return RoomType.PUBLIC.equals(type);
    }

    public boolean isPrivate() {
        return RoomType.PRIVATE.equals(type);
    }

    public void add(User participant) {
        if(participants.size() == LIMIT_PARTICIPANTS) {
            throw new FullRoomException();
        }
        if(participants.contains(participant)) {
            throw new AlreadyParticipateRoomException();
        }
        participants.add(participant);
    }

    public void remove(User user) {
        participants.remove(user);
    }
    
}
