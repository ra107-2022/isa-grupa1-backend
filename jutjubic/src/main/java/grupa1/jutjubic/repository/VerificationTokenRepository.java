package grupa1.jutjubic.repository;

import grupa1.jutjubic.model.User;
import grupa1.jutjubic.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);
    VerificationToken findByUser(User user);
}
