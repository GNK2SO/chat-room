package com.gnk2so.chatroom.user.controller;

import java.net.URI;
import java.security.Principal;

import javax.validation.Valid;

import com.gnk2so.chatroom.user.controller.request.SaveUserRequest;
import com.gnk2so.chatroom.user.model.User;
import com.gnk2so.chatroom.user.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

@RestController
@RequestMapping("/users")
@Api(tags = "Users")
public class UserController {
    
    @Autowired
    private UserService service;

    @PostMapping
    @ApiOperation(value = "Create new user")
    @ApiResponses({
        @ApiResponse(code = 201, message = "Created"),
        @ApiResponse(code = 400, message = "Bad Request"),
        @ApiResponse(code = 409, message = "Already used email"),
    })
    public ResponseEntity<Void> saveUser(
        @Valid @RequestBody SaveUserRequest request,
        UriComponentsBuilder uriBuilder
    ) {
        service.save(request.getUser());
        URI uri = uriBuilder.path("/users/me").build().toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/me")
    @ApiOperation(
        value = "Get user details",
        authorizations = { @Authorization(value="JWT") }
    )
    @ApiResponses({
        @ApiResponse(code = 200, message = "Ok"),
        @ApiResponse(code = 401, message = "Invalid/Expired token"),
        @ApiResponse(code = 403, message = "Don't have permission"),
    })
    public ResponseEntity<User> getUser(Principal principal) {
        User user  = service.findByEmail(principal.getName());
        return ResponseEntity.ok(user);
    }

}
