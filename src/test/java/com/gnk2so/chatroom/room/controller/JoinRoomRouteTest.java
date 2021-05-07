package com.gnk2so.chatroom.room.controller;

import com.gnk2so.chatroom.ControllerTest;
import com.gnk2so.chatroom.config.security.CustomAccessHandler;
import com.gnk2so.chatroom.config.security.CustomAuthEntryPoint;
import com.gnk2so.chatroom.room.controlller.request.JoinRoomRequest;
import com.gnk2so.chatroom.room.exception.AlreadyParticipateRoomException;
import com.gnk2so.chatroom.room.exception.FullRoomException;
import com.gnk2so.chatroom.room.exception.InvalidRoomPasswordException;
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

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class JoinRoomRouteTest extends ControllerTest {
    
    private final String ROUTE = "/rooms/%d/join";
    
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
    public void shouldReturnStatusCodeNoContentWhenUserJoinRoomSuccessfully() throws Exception {
        User user = userRepository.save(UserMock.buildSecured());
        Room room = roomRepository.save(Room.publicRoom("Dev Room"));
        String URL = String.format(ROUTE, room.getId());
        JoinRoomRequest request = new JoinRoomRequest(null);

        doPutRequest(URL, authToken(user.getEmail()), request)
            .andExpect(status().isNoContent());
    }

    @Test
    public void shouldReturnStatusCodeUnauthorizedWhenTriesJoinRoomWithEmptyBearer() throws Exception {
        JoinRoomRequest request = new JoinRoomRequest(null);
        String URL = String.format(ROUTE, 1L);

        doPutRequest(URL, "", request)
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.status").value(UNAUTHORIZED.value()))
            .andExpect(jsonPath("$.message").value(CustomAuthEntryPoint.MESSAGE));
    }

    @Test
    public void shouldReturnStatusCodeUnauthorizedWhenTriesGetRoomWithInvalidBearer() throws Exception {
        JoinRoomRequest request = new JoinRoomRequest(null);
        String URL = String.format(ROUTE, 1L);

        doPutRequest(URL, "Bearer 13456", request)
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.status").value(UNAUTHORIZED.value()))
            .andExpect(jsonPath("$.message").value(CustomAuthEntryPoint.MESSAGE));
    }

    @Test
    public void shouldReturnStatusCodeForbiddenWhenTriesGetRoomWithRefreshToken() throws Exception {
        User user = userRepository.save(UserMock.buildSecured());
        String URL = String.format(ROUTE, 1L);
        JoinRoomRequest request = new JoinRoomRequest(null);

        doPutRequest(URL, refreshToken(user.getEmail()), request)
        .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.status").value(FORBIDDEN.value()))
            .andExpect(jsonPath("$.message").value(CustomAccessHandler.MESSAGE));
    }


    @Test
    public void shouldReturnStatusCodeForbiddenWhenUserTriesJoinPrivateRoomSendingInvalidPassword() throws Exception {
        User user = userRepository.save(UserMock.buildSecured());
        Room room = roomRepository.save(Room.privateRoom("Dev Room", "P@ssw0rd"));
        String URL = String.format(ROUTE, room.getId());
        JoinRoomRequest request = new JoinRoomRequest("WrongPassword");

        doPutRequest(URL, authToken(user.getEmail()), request)
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.status").value(FORBIDDEN.value()))
            .andExpect(jsonPath("$.message").value(InvalidRoomPasswordException.MESSAGE));
    }


    @Test
    public void shouldReturnStatusCodeForbiddenWhenUserTriesJoinFullRoom() throws Exception {
        List<User> users = userRepository.saveAll(UserMock.buildList(11));
        Room room = roomRepository.save(RoomMock.publicRoom("Dev Room", users.subList(0, 10)));
        String URL = String.format(ROUTE, room.getId());
        JoinRoomRequest request = new JoinRoomRequest(null);

        doPutRequest(URL, authToken(users.get(10).getEmail()), request)
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.status").value(FORBIDDEN.value()))
            .andExpect(jsonPath("$.message").value(FullRoomException.MESSAGE));
    }


    @Test
    public void shouldReturnStatusCodeNotFoundWhenUserTriesJoinNonExistentRoom() throws Exception {
        User user = userRepository.save(UserMock.buildSecured());
        String URL = String.format(ROUTE, 1L);
        JoinRoomRequest request = new JoinRoomRequest(null);

        doPutRequest(URL, authToken(user.getEmail()), request)
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(NOT_FOUND.value()))
            .andExpect(jsonPath("$.message").value(RoomNotFoundException.MESSAGE));
    }


    @Test
    public void shouldReturnStatusCodeConflictWhenUserTriesJoinRoomThatAlreadyParticipates() throws Exception {
        User user = userRepository.save(UserMock.buildSecured());
        Room room = roomRepository.save(RoomMock.publicRoom("Dev Room", user));
        String URL = String.format(ROUTE, room.getId());
        JoinRoomRequest request = new JoinRoomRequest(null);

        doPutRequest(URL, authToken(user.getEmail()), request)
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.status").value(CONFLICT.value()))
            .andExpect(jsonPath("$.message").value(AlreadyParticipateRoomException.MESSAGE));
    }
}
