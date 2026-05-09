package grupa1.jutjubic.service;

import grupa1.jutjubic.dto.UserRequest;
import grupa1.jutjubic.model.User;
import grupa1.jutjubic.model.VerificationToken;

import java.util.List;

public interface IUserService {
    User findById(Long id);
    User findByUsername(String username);
    User findByEmail(String email);
    List<User> findAll();
    User save(UserRequest request);
    User update(User user);
    Integer enable(String token);
    Integer resendVerificationEmail(String email);
}
