package grupa1.jutjubic.controller;

import grupa1.jutjubic.dto.JwtAuthRequest;
import grupa1.jutjubic.dto.UserRequest;
import grupa1.jutjubic.dto.UserTokenState;
import grupa1.jutjubic.exception.ResourceConflictException;
import grupa1.jutjubic.model.User;
import grupa1.jutjubic.model.VerificationToken;
import grupa1.jutjubic.repository.VerificationTokenRepository;
import grupa1.jutjubic.service.impl.LoginAttemptService;
import grupa1.jutjubic.service.impl.UserService;
import grupa1.jutjubic.util.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
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

    @Autowired
    private LoginAttemptService loginAttemptService;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthToken(
            @RequestBody JwtAuthRequest authRequest,
            HttpServletRequest request,
            HttpServletResponse response) {

        String ipAddress = getClientIP(request);

        if (!loginAttemptService.tryAttempt(ipAddress)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("Too many login attempts. Please try again later.");
        }

        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authRequest.getEmail(), authRequest.getPassword()));

            loginAttemptService.loginSucceeded(ipAddress);
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            User user = (User) authentication.getPrincipal();
            String jwt = tokenUtils.createToken(user.getEmail(), user.getId());
            
            return ResponseEntity.ok(new UserTokenState(jwt, tokenUtils.getExpiredIn()));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials or account disabled.");
        }
    }

    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null || xfHeader.isEmpty() || !xfHeader.contains(request.getRemoteAddr())) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }

    @PostMapping(value = "/signup", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> addUser(@RequestBody UserRequest request) {
        User existingUser = this.userService.findByUsername(request.getUsername());
        if (existingUser != null) {
            throw new ResourceConflictException(request.getId(), "Username already exists");
        }
        existingUser = this.userService.findByEmail(request.getEmail());
        if (existingUser != null) {
            throw new ResourceConflictException(request.getId(), "Email already exists");
        }

        User user = this.userService.save(request);

        return ResponseEntity.ok("User registered successfully. Please check your email to verify your account.");
    }

    @GetMapping(value = "/verify", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> verifyUser(@RequestParam("token") String token) {
        return switch (userService.enable(token)) {
            case 1 -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid verification token.");
            case 2 -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Verification token has expired.");
            default -> ResponseEntity.ok("Account verified successfully. You can now log in.");
        };
    }

    @PostMapping(value = "/resend-verification", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> resendVerificationEmail(@RequestParam("email") String email) {
        return switch (userService.resendVerificationEmail(email)) {
            case 1 -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with that email does not exist.");
            case 2 -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Account is already verified.");
            default -> ResponseEntity.ok("A new verification email has been sent.");
        };
    }
}
