package com.gnk2so.chatroom.room.controlller;

import java.net.URI;
import java.security.Principal;

import javax.validation.Valid;

import com.gnk2so.chatroom.room.controlller.request.SavePrivateRoomRequest;
import com.gnk2so.chatroom.room.controlller.request.SavePublicRoomRequest;
import com.gnk2so.chatroom.room.model.Room;
import com.gnk2so.chatroom.room.service.RoomService;
import com.gnk2so.chatroom.user.model.User;
import com.gnk2so.chatroom.user.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/rooms")
@Api(tags = "Rooms")
public class RoomController {
    
    @Autowired
    private RoomService roomService;

    @Autowired
    private UserService userService;

    
    @PostMapping("/public")
    @ApiOperation(
        value = "Create new public chat",
        authorizations = { @Authorization(value="JWT") }
    )
    @ApiResponses({
        @ApiResponse(code = 201, message = "Created"),
        @ApiResponse(code = 400, message = "Bad Request"),
    })
    public ResponseEntity<Void> savePublicRoom(
        @Valid @RequestBody SavePublicRoomRequest request,
        Principal principal,
        UriComponentsBuilder uriBuilder
    ) {
        User user = userService.findByEmail(principal.getName());
        Room room = request.getRoom(user);
        Room savedRoom = roomService.save(room);
        String path = String.format("/rooms/%s", savedRoom.getChannel());
        URI uri = uriBuilder.path(path).build().toUri();
        return ResponseEntity.created(uri).build();
    }


    @PostMapping("/private")
    @ApiOperation(
        value = "Create new private chat",
        authorizations = { @Authorization(value="JWT") }
    )
    @ApiResponses({
        @ApiResponse(code = 201, message = "Created"),
        @ApiResponse(code = 400, message = "Bad Request"),
    })
    public ResponseEntity<Void> savePrivateRoom(
        @Valid @RequestBody SavePrivateRoomRequest request,
        Principal principal,
        UriComponentsBuilder uriBuilder
    ) {
        User user = userService.findByEmail(principal.getName());
        Room room = request.getRoom(user);
        Room savedRoom = roomService.save(room);
        String path = String.format("/rooms/%s", savedRoom.getChannel());
        URI uri = uriBuilder.path(path).build().toUri();
        return ResponseEntity.created(uri).build();
    }

}
