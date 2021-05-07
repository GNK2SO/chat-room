package com.gnk2so.chatroom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gnk2so.chatroom.provider.jwt.JwtProvider;

@SpringBootTest
@AutoConfigureMockMvc
public class ControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtProvider jwtProvider;

    protected ResultActions doGetRequest(String url, String token) throws Exception {
        return mockMvc.perform(
            get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
        );
    }

    protected ResultActions doPostRequest(String url, Object content) throws Exception {
        return mockMvc.perform(
            post(url)
                .contentType(MediaType.APPLICATION_JSON)  
                .content(serialize(content))
        );
    }

    protected ResultActions doPostRequest(String url, String token, Object content) throws Exception {
        return mockMvc.perform(
            post(url)
                .contentType(MediaType.APPLICATION_JSON)  
                .header("Authorization", token)
                .content(serialize(content))
        );
    }

    protected ResultActions doPutRequest(String url, String token) throws Exception {
        return mockMvc.perform(
            put(url)
                .contentType(MediaType.APPLICATION_JSON)  
                .header("Authorization", token)
        );
    }

    protected ResultActions doPutRequest(String url, String token, Object content) throws Exception {
        return mockMvc.perform(
            put(url)
                .contentType(MediaType.APPLICATION_JSON)  
                .header("Authorization", token)
                .content(serialize(content))
        );
    }

    protected String serialize(Object obj) throws JsonProcessingException {
        return new Jackson2JsonEncoder().getObjectMapper().writeValueAsString(obj);
    }

    protected String authToken(String email) {
        return String.format("Bearer %s", jwtProvider.createAuthToken(email));
    }

    protected String refreshToken(String email) {
        return String.format("Bearer %s", jwtProvider.createRefreshToken(email));
    }

}
