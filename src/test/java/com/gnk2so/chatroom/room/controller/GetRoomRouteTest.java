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

public class GetRoomRouteTest extends RoomControllerTest {
    
    private final String ROUTE = "/rooms/%s";

    
    @Test
    public void shouldReturnStatusCodeOkAndRoomDetailsWhenUserGetsRoomThatParticipatesSuccefully() throws Exception {
        
        Room room = roomRepository.save(RoomMock.publicRoom("Dev Room", user));
        String URL = String.format(ROUTE, room.getChannel());

        doGetRequest(URL, authToken)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(room.getId()))
            .andExpect(jsonPath("$.title").value(room.getTitle()))
            .andExpect(jsonPath("$.channel").value(room.getChannel()))
            .andExpect(jsonPath("$.type").value(room.getType().toString()))
            .andExpect(jsonPath("$.participants").isNotEmpty())
            .andExpect(jsonPath("$.password").doesNotExist());
    }

    
    @Test
    public void shouldReturnStatusCodeUnauthorizedWhenTryToGetRoomWithEmptyBearer() throws Exception {
        String URL = String.format(ROUTE, "CHANNEL");

        doGetRequest(URL, "")
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.status").value(UNAUTHORIZED.value()))
            .andExpect(jsonPath("$.message").value(CustomAuthEntryPoint.MESSAGE));
    }

    @Test
    public void shouldReturnStatusCodeUnauthorizedWhenTryToGetRoomWithInvalidBearer() throws Exception {
        String URL = String.format(ROUTE, "CHANNEL");

        doGetRequest(URL, "Bearer 13456")
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.status").value(UNAUTHORIZED.value()))
            .andExpect(jsonPath("$.message").value(CustomAuthEntryPoint.MESSAGE));
    }


    @Test
    public void shouldReturnStatusCodeForbiddenWhenTryToGetRoomWithRefreshToken() throws Exception {
        
        String URL = String.format(ROUTE, "CHANNEL");

        doGetRequest(URL, refreshToken(user.getEmail()))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.status").value(FORBIDDEN.value()))
            .andExpect(jsonPath("$.message").value(CustomAccessHandler.MESSAGE));
    }

    @Test
    public void shouldReturnStatusCodeForbiddenWhenUserTryToGetsRoomThatNotParticipates() throws Exception {
        
        Room room = roomRepository.save(Room.publicRoom("Dev Room"));
        String URL = String.format(ROUTE, room.getChannel());

        doGetRequest(URL, authToken)
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.status").value(FORBIDDEN.value()))
            .andExpect(jsonPath("$.message").value(DontParticipateRoomException.MESSAGE));
    }

    @Test
    public void shouldReturnStatusCodeNotFoundWhenUserTryToGetsNonExistentRoom() throws Exception {
        
        String URL = String.format(ROUTE, "CHANNEL");

        doGetRequest(URL, authToken)
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(NOT_FOUND.value()))
            .andExpect(jsonPath("$.message").value(RoomNotFoundException.MESSAGE));
    }
}
