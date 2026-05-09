package grupa1.jutjubic.controller;

import grupa1.jutjubic.dto.WatchPartyRoomResponse;
import grupa1.jutjubic.model.User;
import grupa1.jutjubic.model.WatchPartyRoom;
import grupa1.jutjubic.service.IWatchPartyService;
import grupa1.jutjubic.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping(
        value = "/api/watch-party",
        produces = MediaType.APPLICATION_JSON_VALUE)
public class WatchPartyRoomController {

    @Autowired
    private IWatchPartyService watchPartyService;

    @Autowired
    private UserService userService;


    @PostMapping("/rooms")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<WatchPartyRoomResponse> createRoom (Principal user) {

        User owner = userService.findByUsername(user.getName());
        WatchPartyRoom room = watchPartyService.createRoom(owner);

        WatchPartyRoomResponse response = new WatchPartyRoomResponse();

        response.setCode(room.getCode());
        response.setOwnerId(owner.getId());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/rooms/{code}")
    public ResponseEntity<WatchPartyRoom> getRoom (@PathVariable String code) {

        return ResponseEntity.ok(watchPartyService.getByCode(code));
    }


}
