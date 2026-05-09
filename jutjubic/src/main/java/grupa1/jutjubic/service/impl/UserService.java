package grupa1.jutjubic.service.impl;

import grupa1.jutjubic.dto.UserRequest;
import grupa1.jutjubic.model.User;
import grupa1.jutjubic.model.VerificationToken;
import grupa1.jutjubic.repository.UserRepository;
import grupa1.jutjubic.repository.VerificationTokenRepository;
import grupa1.jutjubic.service.IUserService;
import org.antlr.v4.runtime.misc.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService, UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException(email);
        } else {
            return user;
        }
    }

    public User findByUsername(String username) throws UsernameNotFoundException {
        return  userRepository.findByUsername(username);
    }

    public User findByEmail(String email) throws UsernameNotFoundException {
        return  userRepository.findByEmail(email);
    }

    public User findById(Long id) throws AccessDeniedException, UsernameNotFoundException {
        return userRepository.findById(id).orElse(null);
    }

    public List<User> findAll() throws AccessDeniedException {
        return userRepository.findAll();
    }

    public User update(User user) {
        return userRepository.save(user);
    }

    public Integer enable(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);

        if (verificationToken == null) {
            return 1;
        }

        if (verificationToken.isExpired()) {
            return 2;
        }

        User user = verificationToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);

        verificationTokenRepository.delete(verificationToken);
        return 0;
    }

    @Override
    public User save(UserRequest request) {
        User user = new User();

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setSurname(request.getSurname());
        user.setAddress(request.getAddress());
        user.setEnabled(false);
        user.setRoles(roleService.findByName("ROLE_USER"));

        userRepository.save(user);

        VerificationToken verificationToken = new VerificationToken(user);
        verificationTokenRepository.save(verificationToken);

        String verificationLink = "http://localhost:4200/verify?token=" + verificationToken.getToken();
        emailService.sendVerificationEmail(user.getEmail(), verificationLink);

        return user;
    }

    public Integer resendVerificationEmail(String email) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            return 1;
        }

        if (user.isEnabled()) {
            return 2;
        }

        VerificationToken oldToken = verificationTokenRepository.findByUser(user);
        if (oldToken != null) {
            verificationTokenRepository.delete(oldToken);
        }

        VerificationToken newToken = new VerificationToken(user);
        verificationTokenRepository.save(newToken);

        String verificationLink = "http://localhost:4200/verify?token=" + newToken.getToken();
        emailService.sendVerificationEmail(user.getEmail(), verificationLink);

        return 0;
    }
}
