package com.gnk2so.chatroom.room.controlller;

import java.net.URI;
import java.security.Principal;

import javax.validation.Valid;

import com.gnk2so.chatroom.room.controlller.request.JoinRoomRequest;
import com.gnk2so.chatroom.room.controlller.request.SavePrivateRoomRequest;
import com.gnk2so.chatroom.room.controlller.request.SavePublicRoomRequest;
import com.gnk2so.chatroom.room.exception.DontParticipateRoomException;
import com.gnk2so.chatroom.room.exception.InvalidRoomPasswordException;
import com.gnk2so.chatroom.room.model.Room;
import com.gnk2so.chatroom.room.service.RoomService;
import com.gnk2so.chatroom.user.model.User;
import com.gnk2so.chatroom.user.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
        authorizations = { @Authorization(value="JWT") })
    @ApiResponses({
        @ApiResponse(code = 201, message = "Created"),
        @ApiResponse(code = 400, message = "Bad Request"),
        @ApiResponse(code = 401, message = "Invalid/Expired token"),
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
        authorizations = { @Authorization(value="JWT") })
    @ApiResponses({
        @ApiResponse(code = 201, message = "Created"),
        @ApiResponse(code = 400, message = "Bad Request"),
        @ApiResponse(code = 401, message = "Invalid/Expired token"),
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


    @PutMapping("/{id}/join")
    @ApiOperation(
        value = "Join a room",
        authorizations = { @Authorization(value="JWT") })
    @ApiResponses({
        @ApiResponse(code = 204, message = "No Content"),
        @ApiResponse(code = 401, message = "Invalid/Expired token"),
        @ApiResponse(code = 403, message = "Don't have permission"),
        @ApiResponse(code = 404, message = "Not Found"),
    })
    public ResponseEntity<Room> joinRoom(
        @RequestBody JoinRoomRequest request,
        @PathVariable("id") Long roomID,
        Principal principal
    ) {
        User user = userService.findByEmail(principal.getName());
        Room room = roomService.findById(roomID);
        if(room.isPublic() || room.validate(request.getPassword())) {
            room.add(user);
            roomService.save(room);
            return ResponseEntity.noContent().build();
        }
        throw new InvalidRoomPasswordException();
    }


    @GetMapping("/{channel}")
    @ApiOperation(
        value = "Get room details",
        authorizations = { @Authorization(value="JWT") })
    @ApiResponses({
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 401, message = "Invalid/Expired token"),
        @ApiResponse(code = 403, message = "Don't have permission"),
        @ApiResponse(code = 404, message = "Not Found"),
    })
    public ResponseEntity<Room> savePublicRoom(
        @PathVariable String channel,
        Principal principal
    ) {
        User user = userService.findByEmail(principal.getName());
        Room room = roomService.findByChannel(channel);
        if(room.hasParticipant(user)) {
            return ResponseEntity.ok(room);
        }
        throw new DontParticipateRoomException();
    }

    @PutMapping("/{id}/leave")
    @ApiOperation(
        value = "Leave room",
        authorizations = { @Authorization(value="JWT") })
    @ApiResponses({
        @ApiResponse(code = 204, message = "No Content"),
        @ApiResponse(code = 401, message = "Invalid/Expired token"),
        @ApiResponse(code = 403, message = "Don't have permission"),
        @ApiResponse(code = 404, message = "Not Found"),
    })
    public ResponseEntity<Room> leaveRoom(
        @PathVariable("id") Long roomID,
        Principal principal
    ) {
        User user = userService.findByEmail(principal.getName());
        Room room = roomService.findById(roomID);
        if(room.hasParticipant(user)) {
            room.remove(user);
            roomService.save(room);
            return ResponseEntity.noContent().build();
        }
        throw new DontParticipateRoomException();
    }
}
