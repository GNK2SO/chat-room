package com.gnk2so.chatroom.user.controller;

import javax.validation.Valid;

import com.gnk2so.chatroom.commons.BaseController;
import com.gnk2so.chatroom.user.controller.request.SaveUserRequest;
import com.gnk2so.chatroom.user.model.User;
import com.gnk2so.chatroom.user.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@Api(tags = "Users")
@RequiredArgsConstructor
public class UserController extends BaseController {
    
    private final UserService service;

    @PostMapping
    @ApiOperation(value = "Create new user")
    @ApiResponses({
        @ApiResponse(code = 201, message = "Created"),
        @ApiResponse(code = 400, message = "Bad Request"),
        @ApiResponse(code = 409, message = "Already used email"),
    })
    public ResponseEntity<Void> saveUser( @Valid @RequestBody SaveUserRequest request ) {
        service.save(request.getUser());
        return ResponseEntity.created(getURI("/users/me")).build();
    }

    @GetMapping("/me")
    @ApiOperation(
        value = "Get user details",
        authorizations = { @Authorization(value="JWT") })
    @ApiResponses({
        @ApiResponse(code = 200, message = "Ok"),
        @ApiResponse(code = 401, message = "Invalid/Expired token"),
        @ApiResponse(code = 403, message = "Don't have permission"),
    })
    public ResponseEntity<User> getUser() {
        User user  = service.findByEmail(getPrincipalName());
        return ResponseEntity.ok(user);
    }

}
