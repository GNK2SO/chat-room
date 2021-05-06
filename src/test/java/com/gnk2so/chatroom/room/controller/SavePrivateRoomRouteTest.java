package com.gnk2so.chatroom.room.controller;

import com.gnk2so.chatroom.ControllerTest;
import com.gnk2so.chatroom.config.security.CustomAccessHandler;
import com.gnk2so.chatroom.config.security.CustomAuthEntryPoint;
import com.gnk2so.chatroom.room.controlller.request.SavePrivateRoomRequest;
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

public class SavePrivateRoomRouteTest extends ControllerTest {
    
    private final String ROUTE = "/rooms/private";

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
    public void shouldReturnStatusCodeCreatedWhenSavePrivateRoomSuccessfully() throws Exception 
    {
        User user = userRepository.save(UserMock.build());
        SavePrivateRoomRequest content = new SavePrivateRoomRequest("Dev Room", "P@ssw0rd");

        doPostRequest(ROUTE, authToken(user.getEmail()), content)
            .andExpect(status().isCreated());
    }

    @Test
    public void shouldReturnStatusCodeBadRequestWhenTrySavePrivateRoomWithNullTitle() throws Exception 
    {
        User user = userRepository.save(UserMock.build());
        SavePrivateRoomRequest content = new SavePrivateRoomRequest(null, "P@ssw0rd");
        
        doPostRequest(ROUTE, authToken(user.getEmail()), content)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errors[0].error").value("must not be null"));
    }


    @Test
    public void shouldReturnStatusCodeBadRequestWhenTrySavePrivateRoomWithTitleSizeLowerThanSix() throws Exception 
    {
        User user = userRepository.save(UserMock.build());
        SavePrivateRoomRequest content = new SavePrivateRoomRequest("Four", "P@ssw0rd");
        
        doPostRequest(ROUTE, authToken(user.getEmail()), content)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errors[0].error").value("size must be between 6 and 24"));
    }


    @Test
    public void shouldReturnStatusCodeBadRequestWhenTrySavePrivateRoomWithTitleSizeGreaterThanTwentyFour() throws Exception 
    {
        User user = userRepository.save(UserMock.build());
        String bigTitle = "ThisTitleIsGreaterThenTwentyFour";
        SavePrivateRoomRequest content = new SavePrivateRoomRequest(bigTitle, "P@ssw0rd");
        
        doPostRequest(ROUTE, authToken(user.getEmail()), content)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errors[0].error").value("size must be between 6 and 24"));
    }


    @Test
    public void shouldReturnStatusCodeBadRequestWhenTrySavePrivateRoomWithNullPassword() throws Exception 
    {
        User user = userRepository.save(UserMock.build());
        SavePrivateRoomRequest content = new SavePrivateRoomRequest("Dev Room", null);
        
        doPostRequest(ROUTE, authToken(user.getEmail()), content)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errors[0].error").value("must not be null"));
    }


    @Test
    public void shouldReturnStatusCodeBadRequestWhenTrySavePrivateRoomWithPasswordSizeLowerThanSix() throws Exception 
    {
        User user = userRepository.save(UserMock.build());
        SavePrivateRoomRequest content = new SavePrivateRoomRequest("Dev Room", "Four");
        
        doPostRequest(ROUTE, authToken(user.getEmail()), content)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errors[0].error").value("size must be between 6 and 16"));
    }


    @Test
    public void shouldReturnStatusCodeBadRequestWhenTrySavePrivateRoomWithPasswordSizeGreaterThanSixteen() throws Exception 
    {
        User user = userRepository.save(UserMock.build());
        String bigTitle = "ThisPasswordIsGreaterThenSixteen";
        SavePrivateRoomRequest content = new SavePrivateRoomRequest("Dev Room", bigTitle);
        
        doPostRequest(ROUTE, authToken(user.getEmail()), content)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errors[0].error").value("size must be between 6 and 16"));
    }


    @Test
    public void shouldReturnStatusCodeUnauthorizedWhenTrySavePrivateRoomWithEmptyBearer() throws Exception 
    {
        SavePrivateRoomRequest content = new SavePrivateRoomRequest("Dev Room", "P@ssw0rd");

        doPostRequest(ROUTE, "Bearer 123345", content)
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.status").value(UNAUTHORIZED.value()))
            .andExpect(jsonPath("$.message").value(CustomAuthEntryPoint.MESSAGE));
    }


    @Test
    public void shouldReturnStatusCodeUnauthorizedWhenTrySavePrivateRoomWithInvalidBearer() throws Exception 
    {
        SavePrivateRoomRequest content = new SavePrivateRoomRequest("Dev Room", "P@ssw0rd");

        doPostRequest(ROUTE, "Bearer 123345", content)
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.status").value(UNAUTHORIZED.value()))
            .andExpect(jsonPath("$.message").value(CustomAuthEntryPoint.MESSAGE));
    }


    @Test
    public void shouldReturnStatusCodeForbiddenWhenTrySavePrivateRoomWithRefreshBearer() throws Exception 
    {
        User user = userRepository.save(UserMock.build());
        SavePrivateRoomRequest content = new SavePrivateRoomRequest("Dev Room", "P@ssw0rd");

        doPostRequest(ROUTE, refreshToken(user.getEmail()), content)
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.status").value(FORBIDDEN.value()))
            .andExpect(jsonPath("$.message").value(CustomAccessHandler.MESSAGE));
    }

}
