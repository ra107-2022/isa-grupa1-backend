package grupa1.jutjubic.repository;

import grupa1.jutjubic.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface UserProfileRepository extends JpaRepository<User, Long>
{

}
