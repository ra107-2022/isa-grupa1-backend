package grupa1.jutjubic.controller;

import grupa1.jutjubic.dto.JwtAuthRequest;
import grupa1.jutjubic.dto.UserRequest;
import grupa1.jutjubic.dto.UserTokenState;
import grupa1.jutjubic.exception.ResourceConflictException;
import grupa1.jutjubic.model.User;
import grupa1.jutjubic.service.impl.UserService;
import grupa1.jutjubic.util.TokenUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping(value = "api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {
    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<UserTokenState> createAuthToken(
            @RequestBody JwtAuthRequest authRequest,
            HttpServletResponse response) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authRequest.getEmail(), authRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = (User)  authentication.getPrincipal();
        String jwt = tokenUtils.createToken(user.getEmail(), user.getId());
        int expiredIn = tokenUtils.getExpiredIn();

        return ResponseEntity.ok(new UserTokenState(jwt, expiredIn));
    }

    @PostMapping("/signup")
    public ResponseEntity<UserTokenState> addUser(@RequestBody UserRequest request) {
        User existingUser = this.userService.findByUsername(request.getUsername());
        if (existingUser != null) {
            throw new ResourceConflictException(request.getId(), "Username already exists");
        }
        existingUser = this.userService.findByEmail(request.getEmail());
        if (existingUser != null) {
            throw new ResourceConflictException(request.getId(), "Email already exists");
        }

        User user = this.userService.save(request);

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(), request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenUtils.createToken(user.getEmail(), user.getId());
        int expiredIn = tokenUtils.getExpiredIn();

        return ResponseEntity.ok(new UserTokenState(jwt, expiredIn));
    }
}
