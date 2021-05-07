package com.gnk2so.chatroom.room.controller;

import com.gnk2so.chatroom.ControllerTest;
import com.gnk2so.chatroom.config.security.CustomAccessHandler;
import com.gnk2so.chatroom.config.security.CustomAuthEntryPoint;
import com.gnk2so.chatroom.room.exception.DontParticipateRoomException;
import com.gnk2so.chatroom.room.exception.RoomNotFoundException;
import com.gnk2so.chatroom.room.mock.RoomMock;
import com.gnk2so.chatroom.room.model.Room;
import com.gnk2so.chatroom.room.repository.RoomRepository;
import com.gnk2so.chatroom.user.mock.UserMock;
import com.gnk2so.chatroom.user.model.User;
import com.gnk2so.chatroom.user.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class GetRoomRouteTest extends ControllerTest {
    
    private final String ROUTE = "/rooms/%s";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    @BeforeEach
    public void setup() {
        roomRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void shouldReturnStatusCodeOkAndRoomDetailsWhenUserGetsRoomThatParticipatesSuccefully() throws Exception {
        User user = userRepository.save(UserMock.buildSecured());
        Room room = roomRepository.save(RoomMock.publicRoom("Dev Room", user));
        String URL = String.format(ROUTE, room.getChannel());

        doGetRequest(URL, authToken(user.getEmail()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(room.getId()))
            .andExpect(jsonPath("$.title").value(room.getTitle()))
            .andExpect(jsonPath("$.channel").value(room.getChannel()))
            .andExpect(jsonPath("$.type").value(room.getType().toString()))
            .andExpect(jsonPath("$.participants").isNotEmpty())
            .andExpect(jsonPath("$.password").doesNotExist());
    }

    
    @Test
    public void shouldReturnStatusCodeUnauthorizedWhenTriesGetRoomWithEmptyBearer() throws Exception {
        String URL = String.format(ROUTE, "CHANNEL");

        doGetRequest(URL, "")
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.status").value(UNAUTHORIZED.value()))
            .andExpect(jsonPath("$.message").value(CustomAuthEntryPoint.MESSAGE));
    }

    @Test
    public void shouldReturnStatusCodeUnauthorizedWhenTriesGetRoomWithInvalidBearer() throws Exception {
        String URL = String.format(ROUTE, "CHANNEL");

        doGetRequest(URL, "Bearer 13456")
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.status").value(UNAUTHORIZED.value()))
            .andExpect(jsonPath("$.message").value(CustomAuthEntryPoint.MESSAGE));
    }


    @Test
    public void shouldReturnStatusCodeForbiddenWhenTriesGetRoomWithRefreshToken() throws Exception {
        User user = userRepository.save(UserMock.buildSecured());
        String URL = String.format(ROUTE, "CHANNEL");

        doGetRequest(URL, refreshToken(user.getEmail()))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.status").value(FORBIDDEN.value()))
            .andExpect(jsonPath("$.message").value(CustomAccessHandler.MESSAGE));
    }

    @Test
    public void shouldReturnStatusCodeForbiddenWhenUserTriesGetsRoomThatNotParticipates() throws Exception {
        User user = userRepository.save(UserMock.buildSecured());
        Room room = roomRepository.save(Room.publicRoom("Dev Room"));
        String URL = String.format(ROUTE, room.getChannel());

        doGetRequest(URL, authToken(user.getEmail()))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.status").value(FORBIDDEN.value()))
            .andExpect(jsonPath("$.message").value(DontParticipateRoomException.MESSAGE));
    }

    @Test
    public void shouldReturnStatusCodeNotFoundWhenUserTriesGetsNonExistentRoom() throws Exception {
        User user = userRepository.save(UserMock.buildSecured());
        String URL = String.format(ROUTE, "CHANNEL");

        doGetRequest(URL, authToken(user.getEmail()))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(NOT_FOUND.value()))
            .andExpect(jsonPath("$.message").value(RoomNotFoundException.MESSAGE));
    }
}
