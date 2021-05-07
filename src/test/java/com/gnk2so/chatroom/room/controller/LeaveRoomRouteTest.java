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

public class LeaveRoomRouteTest extends ControllerTest {
 
    private final String ROUTE = "/rooms/%d/leave";
    
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
    public void shouldReturnStatusCodeNoContentWhenUserLeaveRoomSuccefully() throws Exception {
        User user = userRepository.save(UserMock.buildSecured());
        Room room = roomRepository.save(RoomMock.publicRoom("Dev Room", user));
        String URL = String.format(ROUTE, room.getId());

        doPutRequest(URL, authToken(user.getEmail()))
            .andExpect(status().isNoContent());
    }


    @Test
    public void shouldReturnStatusCodeUnauthorizedWhenTriesLeaveRoomWithEmptyBearer() throws Exception {
        String URL = String.format(ROUTE, 1L);

        doPutRequest(URL, "")
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.status").value(UNAUTHORIZED.value()))
            .andExpect(jsonPath("$.message").value(CustomAuthEntryPoint.MESSAGE));
    }

    @Test
    public void shouldReturnStatusCodeUnauthorizedWhenTriesLeaveRoomWithInvalidBearer() throws Exception {
        String URL = String.format(ROUTE, 1L);

        doPutRequest(URL, "Bearer 13456")
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.status").value(UNAUTHORIZED.value()))
            .andExpect(jsonPath("$.message").value(CustomAuthEntryPoint.MESSAGE));
    }

    @Test
    public void shouldReturnStatusCodeForbiddenWhenTriesLeaveRoomWithRefreshToken() throws Exception {
        User user = userRepository.save(UserMock.buildSecured());
        String URL = String.format(ROUTE, 1L);

        doPutRequest(URL, refreshToken(user.getEmail()))
        .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.status").value(FORBIDDEN.value()))
            .andExpect(jsonPath("$.message").value(CustomAccessHandler.MESSAGE));
    }


    @Test
    public void shouldReturnStatusCodeForbiddenWhenUserTriesLeaveRoomThatNotParticipate() throws Exception {
        User user = userRepository.save(UserMock.buildSecured());
        Room room = roomRepository.save(Room.publicRoom("Dev Room"));
        String URL = String.format(ROUTE, room.getId());

        doPutRequest(URL, authToken(user.getEmail()))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.status").value(FORBIDDEN.value()))
            .andExpect(jsonPath("$.message").value(DontParticipateRoomException.MESSAGE));
    }


    @Test
    public void shouldReturnStatusCodeNotFoundWhenUserTriesLeaveNonExistentRoom() throws Exception {
        User user = userRepository.save(UserMock.buildSecured());
        String URL = String.format(ROUTE, 1L);

        doPutRequest(URL, authToken(user.getEmail()))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(NOT_FOUND.value()))
            .andExpect(jsonPath("$.message").value(RoomNotFoundException.MESSAGE));
    }
}
