package grupa1.jutjubic.repository;

import grupa1.jutjubic.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User findByUsername(String username);
    int countByLastActiveAtAfter(Instant time);
}
