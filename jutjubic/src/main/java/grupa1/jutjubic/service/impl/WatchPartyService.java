package grupa1.jutjubic.service.impl;

import grupa1.jutjubic.model.User;
import grupa1.jutjubic.model.WatchPartyRoom;
import grupa1.jutjubic.repository.WatchPartyRoomRepository;
import grupa1.jutjubic.service.IWatchPartyService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class WatchPartyService implements IWatchPartyService {

    private final WatchPartyRoomRepository roomRepository;

    public WatchPartyService(WatchPartyRoomRepository roomRepository){
        this.roomRepository = roomRepository;
    }

    @Override
    public WatchPartyRoom createRoom(User owner){
        WatchPartyRoom room = new WatchPartyRoom();
        room.setOwner(owner);
        room.setCode(generateCode());

        return roomRepository.save(room);
    }

    @Override
    public WatchPartyRoom getByCode(String code){
        return roomRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Room not find."));
    }

    private String generateCode(){
        return UUID.randomUUID().toString().substring(0,6).toUpperCase();
        // kreira random UUID, pretvara taj obj u string i uzima prvih 6 karaktera
    }
}
