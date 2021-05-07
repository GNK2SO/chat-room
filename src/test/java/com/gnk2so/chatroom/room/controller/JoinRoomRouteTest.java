package com.gnk2so.chatroom.room.controller;

import com.gnk2so.chatroom.config.security.CustomAccessHandler;
import com.gnk2so.chatroom.config.security.CustomAuthEntryPoint;
import com.gnk2so.chatroom.room.controlller.request.JoinRoomRequest;
import com.gnk2so.chatroom.room.exception.AlreadyParticipateRoomException;
import com.gnk2so.chatroom.room.exception.FullRoomException;
import com.gnk2so.chatroom.room.exception.InvalidRoomPasswordException;
import com.gnk2so.chatroom.room.exception.RoomNotFoundException;
import com.gnk2so.chatroom.room.mock.JoinRoomRequestMock;
import com.gnk2so.chatroom.room.mock.RoomMock;
import com.gnk2so.chatroom.room.model.Room;
import com.gnk2so.chatroom.user.mock.UserMock;
import com.gnk2so.chatroom.user.model.User;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class JoinRoomRouteTest extends RoomControllerTest {
    
    private final String ROUTE = "/rooms/%d/join";


    @Test
    public void shouldReturnStatusCodeNoContentWhenUserJoinRoomSuccessfully() throws Exception {
        Room room = roomRepository.save(Room.publicRoom("Dev Room"));
        String URL = String.format(ROUTE, room.getId());

        doPutRequest(URL, authToken, JoinRoomRequestMock.build())
            .andExpect(status().isNoContent());
    }

    @Test
    public void shouldReturnStatusCodeUnauthorizedWhenTriesJoinRoomWithEmptyBearer() throws Exception {
        String URL = String.format(ROUTE, 1L);

        doPutRequest(URL, "", JoinRoomRequestMock.build())
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.status").value(UNAUTHORIZED.value()))
            .andExpect(jsonPath("$.message").value(CustomAuthEntryPoint.MESSAGE));
    }

    @Test
    public void shouldReturnStatusCodeUnauthorizedWhenTriesGetRoomWithInvalidBearer() throws Exception {
        String URL = String.format(ROUTE, 1L);

        doPutRequest(URL, "Bearer 13456", JoinRoomRequestMock.build())
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.status").value(UNAUTHORIZED.value()))
            .andExpect(jsonPath("$.message").value(CustomAuthEntryPoint.MESSAGE));
    }

    @Test
    public void shouldReturnStatusCodeForbiddenWhenTriesGetRoomWithRefreshToken() throws Exception {
        String URL = String.format(ROUTE, 1L);

        doPutRequest(URL, refreshToken(user.getEmail()), JoinRoomRequestMock.build())
        .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.status").value(FORBIDDEN.value()))
            .andExpect(jsonPath("$.message").value(CustomAccessHandler.MESSAGE));
    }


    @Test
    public void shouldReturnStatusCodeForbiddenWhenUserTriesJoinPrivateRoomSendingInvalidPassword() throws Exception {
        Room room = roomRepository.save(Room.privateRoom("Dev Room", "P@ssw0rd"));
        String URL = String.format(ROUTE, room.getId());
        JoinRoomRequest request = JoinRoomRequestMock.build("WrongPassword");

        doPutRequest(URL, authToken, request)
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.status").value(FORBIDDEN.value()))
            .andExpect(jsonPath("$.message").value(InvalidRoomPasswordException.MESSAGE));
    }


    @Test
    public void shouldReturnStatusCodeForbiddenWhenUserTriesJoinFullRoom() throws Exception {
        List<User> users = userRepository.saveAll(UserMock.buildList(11));
        Room room = roomRepository.save(RoomMock.publicRoom("Dev Room", users.subList(0, 10)));
        String URL = String.format(ROUTE, room.getId());

        doPutRequest(URL, authToken(users.get(10).getEmail()), JoinRoomRequestMock.build())
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.status").value(FORBIDDEN.value()))
            .andExpect(jsonPath("$.message").value(FullRoomException.MESSAGE));
    }


    @Test
    public void shouldReturnStatusCodeNotFoundWhenUserTriesJoinNonExistentRoom() throws Exception {
        String URL = String.format(ROUTE, 1L);

        doPutRequest(URL, authToken, JoinRoomRequestMock.build())
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(NOT_FOUND.value()))
            .andExpect(jsonPath("$.message").value(RoomNotFoundException.MESSAGE));
    }


    @Test
    public void shouldReturnStatusCodeConflictWhenUserTriesJoinRoomThatAlreadyParticipates() throws Exception {
        Room room = roomRepository.save(RoomMock.publicRoom("Dev Room", user));
        String URL = String.format(ROUTE, room.getId());

        doPutRequest(URL, authToken, JoinRoomRequestMock.build())
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.status").value(CONFLICT.value()))
            .andExpect(jsonPath("$.message").value(AlreadyParticipateRoomException.MESSAGE));
    }
}
