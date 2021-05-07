package com.gnk2so.chatroom.room.controller;

import com.gnk2so.chatroom.ControllerTest;
import com.gnk2so.chatroom.room.repository.RoomRepository;
import com.gnk2so.chatroom.user.mock.UserMock;
import com.gnk2so.chatroom.user.model.User;
import com.gnk2so.chatroom.user.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

public class RoomControllerTest extends ControllerTest {

    protected User user;

    protected String authToken;
    
    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected RoomRepository roomRepository;

    @BeforeEach
    public void setup() {
        roomRepository.deleteAll();
        userRepository.deleteAll();
        user = userRepository.save(UserMock.buildSecured());
        authToken = authToken(user.getEmail());
    }
}
