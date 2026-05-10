package grupa1.jutjubic.service.impl;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class WatchPartySessionTracker {
    private final Map<String, Integer> roomUsers = new HashMap<>();

    public void join(String code){
        roomUsers.put(code, roomUsers.getOrDefault(code, 0)+1);
    }

    public void leave(String code) {
        int current = roomUsers.getOrDefault(code, 0);
        if (current <= 1) {
            roomUsers.remove(code);
        } else {
            roomUsers.put(code, current - 1);
        }
    }

    public int getCount(String code){
        return roomUsers.getOrDefault(code, 0);
    }
}
