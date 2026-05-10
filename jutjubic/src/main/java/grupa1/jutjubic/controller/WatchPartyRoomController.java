package grupa1.jutjubic.controller;

import grupa1.jutjubic.dto.WatchPartyEvent;
import grupa1.jutjubic.dto.WatchPartyRoomResponse;
import grupa1.jutjubic.model.User;
import grupa1.jutjubic.model.WatchPartyRoom;
import grupa1.jutjubic.repository.WatchPartyRoomRepository;
import grupa1.jutjubic.service.IWatchPartyService;
import grupa1.jutjubic.service.impl.UserService;
import grupa1.jutjubic.service.impl.WatchPartySessionTracker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private WatchPartySessionTracker tracker;

    @PostMapping("/rooms")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<WatchPartyRoomResponse> createRoom(Principal user) {
        User owner = userService.findByUsername(user.getName());
        WatchPartyRoom room = watchPartyService.createRoom(owner);

        WatchPartyRoomResponse response = new WatchPartyRoomResponse();
        response.setCode(room.getCode());
        response.setOwnerId(owner.getId());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/rooms/{code}")
    public ResponseEntity<WatchPartyRoom> getRoom(@PathVariable String code) {
        return ResponseEntity.ok(watchPartyService.getByCode(code));
    }

    @PostMapping("/rooms/{code}/join")
    public ResponseEntity<Void> joinRoom(@PathVariable String code) {
        tracker.join(code);

        WatchPartyEvent joinEvent = new WatchPartyEvent();
        joinEvent.setType("USER_JOIN");
        joinEvent.setRoomCode(code);
        joinEvent.setUsersCount(tracker.getCount(code));

        messagingTemplate.convertAndSend("/topic/room/" + code, joinEvent);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/rooms/{code}/leave")
    public ResponseEntity<Void> leaveRoom(@PathVariable String code) {
        tracker.leave(code);

        WatchPartyEvent leaveEvent = new WatchPartyEvent();
        leaveEvent.setType("USER_LEAVE");
        leaveEvent.setRoomCode(code);
        leaveEvent.setUsersCount(tracker.getCount(code));

        messagingTemplate.convertAndSend("/topic/room/" + code, leaveEvent);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/rooms/{code}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> closeRoom(@PathVariable String code, Principal user) {
        WatchPartyEvent closeEvent = new WatchPartyEvent();
        closeEvent.setType("ROOM_CLOSED");
        closeEvent.setRoomCode(code);

        messagingTemplate.convertAndSend("/topic/room/" + code, closeEvent);

        watchPartyService.deleteRoom(code); // obrisi sobu iz baze

        return ResponseEntity.ok().build();
    }

    // WEBSOCKET
    @MessageMapping("/watchparty/play")
    public void playVideo(WatchPartyEvent event) {
        WatchPartyRoom room = watchPartyService.getByCode(event.getRoomCode());
        room.setVideoId(event.getVideoId());
        watchPartyService.save(room);

        messagingTemplate.convertAndSend("/topic/room/" + event.getRoomCode(), event);
    }
}