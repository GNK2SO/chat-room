package com.gnk2so.chatroom.room.controller;

import com.gnk2so.chatroom.config.security.CustomAccessHandler;
import com.gnk2so.chatroom.config.security.CustomAuthEntryPoint;
import com.gnk2so.chatroom.room.mock.SaveRoomRequestMock;

import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class SaveRoomRouteTest extends RoomControllerTest {
    
    private final String ROUTE = "/rooms";
    

    @Test
    public void shouldReturnStatusCodeCreatedWhenSaveRoomSuccessfully() throws Exception 
    {
        doPostRequest(ROUTE, authToken, SaveRoomRequestMock.build())
            .andExpect(status().isCreated());
    }

    @Test
    public void shouldReturnStatusCodeBadRequestWhenTryToSaveRoomWithNullTitle() throws Exception 
    {
        doPostRequest(ROUTE, authToken, SaveRoomRequestMock.build(null))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errors[0].error").value("must not be null"));
    }


    @Test
    public void shouldReturnStatusCodeBadRequestWhenTryToSaveRoomWithTitleSizeLowerThanSix() throws Exception 
    {
        doPostRequest(ROUTE, authToken, SaveRoomRequestMock.build("Four"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errors[0].error").value("size must be between 6 and 24"));
    }


    @Test
    public void shouldReturnStatusCodeBadRequestWhenTryToSaveRoomWithTitleSizeGreaterThanTwentyFour() throws Exception 
    {
        String bigTitle = "ThisTitleIsGreaterThenTwentyFourCharacters";
        
        doPostRequest(ROUTE, authToken, SaveRoomRequestMock.build(bigTitle))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errors[0].error").value("size must be between 6 and 24"));
    }

    @Test
    public void shouldReturnStatusCodeBadRequestWhenTryToSaveRoomWithPasswordSizeGreaterThanSixteen() throws Exception 
    {
        String bigPassword = "ThisPasswordIsGreaterThenSixteenCharacters";
        
        doPostRequest(ROUTE, authToken, SaveRoomRequestMock.build("Dev Room", bigPassword))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errors[0].error").value("size must be lower than 16"));
    }

    @Test
    public void shouldReturnStatusCodeUnauthorizedWhenTryToSaveRoomWithEmptyBearer() throws Exception 
    {
        doPostRequest(ROUTE, "Bearer 123345", SaveRoomRequestMock.build())
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.status").value(UNAUTHORIZED.value()))
            .andExpect(jsonPath("$.message").value(CustomAuthEntryPoint.MESSAGE));
    }


    @Test
    public void shouldReturnStatusCodeUnauthorizedWhenTryToSaveRoomWithInvalidBearer() throws Exception 
    {
        doPostRequest(ROUTE, "Bearer 123345", SaveRoomRequestMock.build())
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.status").value(UNAUTHORIZED.value()))
            .andExpect(jsonPath("$.message").value(CustomAuthEntryPoint.MESSAGE));
    }


    @Test
    public void shouldReturnStatusCodeForbiddenWhenTryToSaveRoomWithRefreshBearer() throws Exception 
    {
        doPostRequest(ROUTE, refreshToken(user.getEmail()), SaveRoomRequestMock.build())
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.status").value(FORBIDDEN.value()))
            .andExpect(jsonPath("$.message").value(CustomAccessHandler.MESSAGE));
    }

}
