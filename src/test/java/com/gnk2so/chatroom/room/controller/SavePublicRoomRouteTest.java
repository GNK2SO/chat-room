package com.gnk2so.chatroom.room.controller;

import com.gnk2so.chatroom.config.security.CustomAccessHandler;
import com.gnk2so.chatroom.config.security.CustomAuthEntryPoint;
import com.gnk2so.chatroom.room.controlller.request.SavePublicRoomRequest;

import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class SavePublicRoomRouteTest extends RoomControllerTest {
    
    private final String ROUTE = "/rooms/public";
    

    @Test
    public void shouldReturnStatusCodeCreatedWhenSavePublicRoomSuccessfully() throws Exception 
    {
        SavePublicRoomRequest content = new SavePublicRoomRequest("Dev Room");

        doPostRequest(ROUTE, authToken, content)
            .andExpect(status().isCreated());
    }

    @Test
    public void shouldReturnStatusCodeBadRequestWhenTryToSavePublicRoomWithNullTitle() throws Exception 
    {
        SavePublicRoomRequest content = new SavePublicRoomRequest(null);
        
        doPostRequest(ROUTE, authToken, content)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errors[0].error").value("must not be null"));
    }


    @Test
    public void shouldReturnStatusCodeBadRequestWhenTryToSavePublicRoomWithTitleSizeLowerThanSix() throws Exception 
    {
        SavePublicRoomRequest content = new SavePublicRoomRequest("Four");
        
        doPostRequest(ROUTE, authToken, content)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errors[0].error").value("size must be between 6 and 24"));
    }


    @Test
    public void shouldReturnStatusCodeBadRequestWhenTryToSavePublicRoomWithTitleSizeGreaterThanTwentyFour() throws Exception 
    {
        String bigTitle = "ThisTitleIsGreaterThenTwentyFourCharacters";
        SavePublicRoomRequest content = new SavePublicRoomRequest(bigTitle);
        
        doPostRequest(ROUTE, authToken, content)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errors[0].error").value("size must be between 6 and 24"));
    }

    @Test
    public void shouldReturnStatusCodeUnauthorizedWhenTryToSavePublicRoomWithEmptyBearer() throws Exception 
    {
        SavePublicRoomRequest content = new SavePublicRoomRequest("Dev Room");

        doPostRequest(ROUTE, "Bearer 123345", content)
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.status").value(UNAUTHORIZED.value()))
            .andExpect(jsonPath("$.message").value(CustomAuthEntryPoint.MESSAGE));
    }


    @Test
    public void shouldReturnStatusCodeUnauthorizedWhenTryToSavePublicRoomWithInvalidBearer() throws Exception 
    {
        SavePublicRoomRequest content = new SavePublicRoomRequest("Dev Room");

        doPostRequest(ROUTE, "Bearer 123345", content)
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.status").value(UNAUTHORIZED.value()))
            .andExpect(jsonPath("$.message").value(CustomAuthEntryPoint.MESSAGE));
    }


    @Test
    public void shouldReturnStatusCodeForbiddenWhenTryToSavePublicRoomWithRefreshBearer() throws Exception 
    {
        SavePublicRoomRequest content = new SavePublicRoomRequest("Dev Room");

        doPostRequest(ROUTE, refreshToken(user.getEmail()), content)
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.status").value(FORBIDDEN.value()))
            .andExpect(jsonPath("$.message").value(CustomAccessHandler.MESSAGE));
    }

}
