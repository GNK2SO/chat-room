package com.gnk2so.chatroom.room.controller;

import com.gnk2so.chatroom.config.security.CustomAccessHandler;
import com.gnk2so.chatroom.config.security.CustomAuthEntryPoint;
import com.gnk2so.chatroom.room.exception.DontParticipateRoomException;
import com.gnk2so.chatroom.room.exception.RoomNotFoundException;
import com.gnk2so.chatroom.room.mock.RoomMock;
import com.gnk2so.chatroom.room.model.Room;

import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class LeaveRoomRouteTest extends RoomControllerTest {
 
    private final String ROUTE = "/rooms/%d/leave";
    

    @Test
    public void shouldReturnStatusCodeNoContentWhenUserLeaveRoomSuccefully() throws Exception {
        Room room = roomRepository.save(RoomMock.publicRoom("Dev Room", user));
        String URL = String.format(ROUTE, room.getId());

        doPutRequest(URL, authToken)
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
        String URL = String.format(ROUTE, 1L);

        doPutRequest(URL, refreshToken(user.getEmail()))
        .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.status").value(FORBIDDEN.value()))
            .andExpect(jsonPath("$.message").value(CustomAccessHandler.MESSAGE));
    }


    @Test
    public void shouldReturnStatusCodeForbiddenWhenUserTriesLeaveRoomThatNotParticipate() throws Exception {
        Room room = roomRepository.save(Room.publicRoom("Dev Room"));
        String URL = String.format(ROUTE, room.getId());

        doPutRequest(URL, authToken)
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.status").value(FORBIDDEN.value()))
            .andExpect(jsonPath("$.message").value(DontParticipateRoomException.MESSAGE));
    }


    @Test
    public void shouldReturnStatusCodeNotFoundWhenUserTriesLeaveNonExistentRoom() throws Exception {
        String URL = String.format(ROUTE, 1L);

        doPutRequest(URL, authToken)
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(NOT_FOUND.value()))
            .andExpect(jsonPath("$.message").value(RoomNotFoundException.MESSAGE));
    }
}
