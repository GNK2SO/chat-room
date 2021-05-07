package com.gnk2so.chatroom.room.controller;

import com.gnk2so.chatroom.config.security.CustomAccessHandler;
import com.gnk2so.chatroom.config.security.CustomAuthEntryPoint;
import com.gnk2so.chatroom.room.controlller.request.SavePrivateRoomRequest;

import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class SavePrivateRoomRouteTest extends RoomControllerTest {
    
    private final String ROUTE = "/rooms/private";


    @Test
    public void shouldReturnStatusCodeCreatedWhenSavePrivateRoomSuccessfully() throws Exception 
    {
        SavePrivateRoomRequest content = new SavePrivateRoomRequest("Dev Room", "P@ssw0rd");

        doPostRequest(ROUTE, authToken, content)
            .andExpect(status().isCreated());
    }

    @Test
    public void shouldReturnStatusCodeBadRequestWhenTryToSavePrivateRoomWithNullTitle() throws Exception 
    {
        SavePrivateRoomRequest content = new SavePrivateRoomRequest(null, "P@ssw0rd");
        
        doPostRequest(ROUTE, authToken, content)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errors[0].error").value("must not be null"));
    }


    @Test
    public void shouldReturnStatusCodeBadRequestWhenTryToSavePrivateRoomWithTitleSizeLowerThanSix() throws Exception 
    {
        SavePrivateRoomRequest content = new SavePrivateRoomRequest("Four", "P@ssw0rd");
        
        doPostRequest(ROUTE, authToken, content)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errors[0].error").value("size must be between 6 and 24"));
    }


    @Test
    public void shouldReturnStatusCodeBadRequestWhenTryToSavePrivateRoomWithTitleSizeGreaterThanTwentyFour() throws Exception 
    {
        String bigTitle = "ThisTitleIsGreaterThenTwentyFour";
        SavePrivateRoomRequest content = new SavePrivateRoomRequest(bigTitle, "P@ssw0rd");
        
        doPostRequest(ROUTE, authToken, content)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errors[0].error").value("size must be between 6 and 24"));
    }


    @Test
    public void shouldReturnStatusCodeBadRequestWhenTryToSavePrivateRoomWithNullPassword() throws Exception 
    {
        SavePrivateRoomRequest content = new SavePrivateRoomRequest("Dev Room", null);
        
        doPostRequest(ROUTE, authToken, content)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errors[0].error").value("must not be null"));
    }


    @Test
    public void shouldReturnStatusCodeBadRequestWhenTryToSavePrivateRoomWithPasswordSizeLowerThanSix() throws Exception 
    {
        SavePrivateRoomRequest content = new SavePrivateRoomRequest("Dev Room", "Four");
        
        doPostRequest(ROUTE, authToken, content)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errors[0].error").value("size must be between 6 and 16"));
    }


    @Test
    public void shouldReturnStatusCodeBadRequestWhenTryToSavePrivateRoomWithPasswordSizeGreaterThanSixteen() throws Exception 
    {
        String bigTitle = "ThisPasswordIsGreaterThenSixteen";
        SavePrivateRoomRequest content = new SavePrivateRoomRequest("Dev Room", bigTitle);
        
        doPostRequest(ROUTE, authToken, content)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errors[0].error").value("size must be between 6 and 16"));
    }


    @Test
    public void shouldReturnStatusCodeUnauthorizedWhenTryToSavePrivateRoomWithEmptyBearer() throws Exception 
    {
        SavePrivateRoomRequest content = new SavePrivateRoomRequest("Dev Room", "P@ssw0rd");

        doPostRequest(ROUTE, "Bearer 123345", content)
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.status").value(UNAUTHORIZED.value()))
            .andExpect(jsonPath("$.message").value(CustomAuthEntryPoint.MESSAGE));
    }


    @Test
    public void shouldReturnStatusCodeUnauthorizedWhenTryToSavePrivateRoomWithInvalidBearer() throws Exception 
    {
        SavePrivateRoomRequest content = new SavePrivateRoomRequest("Dev Room", "P@ssw0rd");

        doPostRequest(ROUTE, "Bearer 123345", content)
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.status").value(UNAUTHORIZED.value()))
            .andExpect(jsonPath("$.message").value(CustomAuthEntryPoint.MESSAGE));
    }


    @Test
    public void shouldReturnStatusCodeForbiddenWhenTryToSavePrivateRoomWithRefreshBearer() throws Exception 
    {
        SavePrivateRoomRequest content = new SavePrivateRoomRequest("Dev Room", "P@ssw0rd");

        doPostRequest(ROUTE, refreshToken(user.getEmail()), content)
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.status").value(FORBIDDEN.value()))
            .andExpect(jsonPath("$.message").value(CustomAccessHandler.MESSAGE));
    }

}
