package grupa1.jutjubic.controller;

import grupa1.jutjubic.dto.UserProfileDTO;
import grupa1.jutjubic.model.User;
import grupa1.jutjubic.service.IUserProfileService;
import grupa1.jutjubic.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserProfileController {

    public static class UserProfileRequest {
        private String username;
        private String name;
        private String surname;
        private String email;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getSurname() { return surname; }
        public void setSurname(String surname) { this.surname = surname; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }

    @Autowired
    private IUserProfileService userProfileService;

    @Autowired
    private IUserService userService; // Da dobijemo User iz username-a

    // GET [za sve korisnike]
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserProfile(@PathVariable Long id) {
        try {
            UserProfileDTO profile = userProfileService.getProfileById(id);
            return ResponseEntity.ok(profile);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    // GET [My profile]
    @GetMapping("/me")
    public ResponseEntity<?> getMyProfile(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("You must be logged in.");
        }

        String username = authentication.getName();
        User loggedUser = userService.findByUsername(username);

        if (loggedUser == null) {
            return ResponseEntity.status(404).body("Logged user not found.");
        }

        try {
            UserProfileDTO profile = userProfileService.getProfileById(loggedUser.getId());
            return ResponseEntity.ok(profile);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    // UPDATE
    @PutMapping("/me")
    public ResponseEntity<?> updateMyProfile(@RequestBody UserProfileRequest request,
                                             Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("You must be logged in.");
        }

        String username = authentication.getName();
        User loggedUser = userService.findByUsername(username);

        if (loggedUser == null) {
            return ResponseEntity.status(404).body("Logged user not found.");
        }

        try {
            UserProfileDTO dto = new UserProfileDTO();
            dto.setUsername(request.getUsername());
            dto.setName(request.getName());
            dto.setSurname(request.getSurname());
            dto.setEmail(request.getEmail());

            UserProfileDTO updated = userProfileService.updateProfile(loggedUser.getId(), dto);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    // DELETE
    @DeleteMapping("/me")
    public ResponseEntity<?> deleteMyProfile(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("You must be logged in.");
        }

        String username = authentication.getName();
        User loggedUser = userService.findByUsername(username);

        if (loggedUser == null) {
            return ResponseEntity.status(404).body("Logged user not found.");
        }

        try {
            userProfileService.deleteProfile(loggedUser.getId());
            return ResponseEntity.ok().body("Profile deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}