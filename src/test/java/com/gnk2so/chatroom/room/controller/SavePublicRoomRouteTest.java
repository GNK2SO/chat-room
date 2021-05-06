package com.gnk2so.chatroom.room.controller;

import com.gnk2so.chatroom.ControllerTest;
import com.gnk2so.chatroom.config.security.CustomAccessHandler;
import com.gnk2so.chatroom.config.security.CustomAuthEntryPoint;
import com.gnk2so.chatroom.room.controlller.request.SavePublicRoomRequest;
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
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class SavePublicRoomRouteTest extends ControllerTest {
    
    private final String ROUTE = "/rooms/public";

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
    public void shouldReturnStatusCodeCreatedWhenSavePublicRoomSuccessfully() throws Exception 
    {
        User user = userRepository.save(UserMock.build());
        SavePublicRoomRequest content = new SavePublicRoomRequest("Dev Room");

        doPostRequest(ROUTE, authToken(user.getEmail()), content)
            .andExpect(status().isCreated());
    }

    @Test
    public void shouldReturnStatusCodeBadRequestWhenTrySavePublicRoomWithNullTitle() throws Exception 
    {
        User user = userRepository.save(UserMock.build());
        SavePublicRoomRequest content = new SavePublicRoomRequest(null);
        
        doPostRequest(ROUTE, authToken(user.getEmail()), content)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errors[0].error").value("must not be null"));
    }


    @Test
    public void shouldReturnStatusCodeBadRequestWhenTrySavePublicRoomWithTitleSizeLowerThanSix() throws Exception 
    {
        User user = userRepository.save(UserMock.build());
        SavePublicRoomRequest content = new SavePublicRoomRequest("Four");
        
        doPostRequest(ROUTE, authToken(user.getEmail()), content)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errors[0].error").value("size must be between 6 and 24"));
    }


    @Test
    public void shouldReturnStatusCodeBadRequestWhenTrySavePublicRoomWithTitleSizeGreaterThanTwentyFour() throws Exception 
    {
        User user = userRepository.save(UserMock.build());
        String bigTitle = "ThisTitleIsGreaterThenTwentyFourCharacters";
        SavePublicRoomRequest content = new SavePublicRoomRequest(bigTitle);
        
        doPostRequest(ROUTE, authToken(user.getEmail()), content)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errors[0].error").value("size must be between 6 and 24"));
    }

    @Test
    public void shouldReturnStatusCodeUnauthorizedWhenTrySavePublicRoomWithEmptyBearer() throws Exception 
    {
        SavePublicRoomRequest content = new SavePublicRoomRequest("Dev Room");

        doPostRequest(ROUTE, "Bearer 123345", content)
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.status").value(UNAUTHORIZED.value()))
            .andExpect(jsonPath("$.message").value(CustomAuthEntryPoint.MESSAGE));
    }


    @Test
    public void shouldReturnStatusCodeUnauthorizedWhenTrySavePublicRoomWithInvalidBearer() throws Exception 
    {
        SavePublicRoomRequest content = new SavePublicRoomRequest("Dev Room");

        doPostRequest(ROUTE, "Bearer 123345", content)
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.status").value(UNAUTHORIZED.value()))
            .andExpect(jsonPath("$.message").value(CustomAuthEntryPoint.MESSAGE));
    }


    @Test
    public void shouldReturnStatusCodeForbiddenWhenTrySavePublicRoomWithRefreshBearer() throws Exception 
    {
        User user = userRepository.save(UserMock.build());
        SavePublicRoomRequest content = new SavePublicRoomRequest("Dev Room");

        doPostRequest(ROUTE, refreshToken(user.getEmail()), content)
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.status").value(FORBIDDEN.value()))
            .andExpect(jsonPath("$.message").value(CustomAccessHandler.MESSAGE));
    }

}
