package grupa1.jutjubic.service;

import grupa1.jutjubic.model.User;
import grupa1.jutjubic.model.WatchPartyRoom;

public interface IWatchPartyService {

    WatchPartyRoom createRoom(User owner);
    WatchPartyRoom getByCode(String code);
    WatchPartyRoom save(WatchPartyRoom room);
    void deleteRoom(String code);
}
