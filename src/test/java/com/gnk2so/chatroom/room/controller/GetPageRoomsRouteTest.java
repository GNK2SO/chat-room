package com.gnk2so.chatroom.room.controller;

import com.gnk2so.chatroom.config.security.CustomAccessHandler;
import com.gnk2so.chatroom.config.security.CustomAuthEntryPoint;
import com.gnk2so.chatroom.room.mock.RoomMock;
import com.gnk2so.chatroom.room.model.Room;

import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static java.lang.String.format;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class GetPageRoomsRouteTest extends RoomControllerTest {
    
    private static final String ROUTE = "/rooms";

    @Test
    public void shouldReturnPageRoomsWhenGetPageRoomsSuccefully() throws Exception 
    {
        int size = 5;
        roomRepository.saveAll(RoomMock.buildList(size));

        doGetRequest(ROUTE, authToken)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.length()").value(size));
    }
    
    
    @Test
    public void shouldReturnEmptyPagesWhenNotFoundRooms() throws Exception 
    {
        doGetRequest(ROUTE, authToken)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.length()").value(0));
    }


    @Test
    public void shouldReturnPageWithTwoRoomsWhenGetPageRoomsWithFilterPerPageEqualsTwo() throws Exception 
    {
        int size = 2;
        String URL = format("%s?size=%d", ROUTE, size);
        roomRepository.saveAll(RoomMock.buildList(5));

        doGetRequest(URL, authToken)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.length()").value(size));
    }


    @Test
    public void shouldReturnPageRoomsWhenGetPageRoomsWithFilterPerPageAndPage() throws Exception 
    {
        int page = 3;
        String URL = format("%s?page=%d&size=%d", ROUTE, page, 1);
        roomRepository.saveAll(RoomMock.buildList(5));

        doGetRequest(URL, authToken)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.length()").value(1));
    }


    @Test
    public void shouldReturnEmptyPageWhenGetPageRoomsWithPageGreaterThanLastPage() throws Exception 
    {
        String URL = format("%s?page=%d&size=%d", ROUTE, 10, 1);
        roomRepository.saveAll(RoomMock.buildList(5));

        doGetRequest(URL, authToken)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.length()").value(0));
    }


    @Test
    public void shouldReturnPageRoomsFilterByTitleWhenGetPageRoomsAndPassTitle() throws Exception 
    {
        List<Room> rooms = RoomMock.buildList(5);
        Room room = rooms.get(3);
        String URL = format("%s?title=%s", ROUTE, room.getTitle());
        roomRepository.saveAll(rooms);

        doGetRequest(URL, authToken)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.length()").value(1))
            .andExpect(jsonPath("$.content[0].id").value(room.getId()))
            .andExpect(jsonPath("$.content[0].title").value(room.getTitle()))
            .andExpect(jsonPath("$.content[0].channel").value(room.getChannel()))
            .andExpect(jsonPath("$.content[0].type").value(room.getType().toString()))
            .andExpect(jsonPath("$.content[0].participants").isEmpty())
            .andExpect(jsonPath("$.content[0].password").doesNotExist());
    }


    @Test
    public void shouldReturnStatusCodeUnauthorizedWhenTryToGetPageRoomsWithEmptyBearer() throws Exception {
        doGetRequest(ROUTE, "")
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.status").value(UNAUTHORIZED.value()))
            .andExpect(jsonPath("$.message").value(CustomAuthEntryPoint.MESSAGE));
    }


    @Test
    public void shouldReturnStatusCodeUnauthorizedWhenTryToGetPageRoomsWithInvalidBearer() throws Exception {
        doGetRequest(ROUTE, "Bearer 13456")
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.status").value(UNAUTHORIZED.value()))
            .andExpect(jsonPath("$.message").value(CustomAuthEntryPoint.MESSAGE));
    }


    @Test
    public void shouldReturnStatusCodeForbiddenWhenTryToGetPageRoomsWithRefreshToken() throws Exception {
        doGetRequest(ROUTE, refreshToken(user.getEmail()))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.status").value(FORBIDDEN.value()))
            .andExpect(jsonPath("$.message").value(CustomAccessHandler.MESSAGE));
    }

}
