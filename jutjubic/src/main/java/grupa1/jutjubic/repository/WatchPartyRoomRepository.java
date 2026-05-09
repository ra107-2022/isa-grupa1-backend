package grupa1.jutjubic.repository;

import grupa1.jutjubic.model.WatchPartyRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WatchPartyRoomRepository extends JpaRepository <WatchPartyRoom, Long> {

    Optional<WatchPartyRoom> findByCode(String code);
}
