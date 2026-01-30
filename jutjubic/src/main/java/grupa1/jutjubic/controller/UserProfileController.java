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

    // GET profile
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserProfile(@PathVariable Long id) {
        try {
            UserProfileDTO profile = userProfileService.getProfileById(id);
            return ResponseEntity.ok(profile);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    // UPDATE profile
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProfile(@PathVariable Long id,
                                           @RequestBody UserProfileRequest request,
                                           Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("You must be logged in to edit profile.");
        }

        String username = authentication.getName();
        User loggedUser = userService.findByUsername(username);
        if (loggedUser == null) {
            return ResponseEntity.status(401).body("Logged user not found.");
        }

        if (!loggedUser.getId().equals(id)) {
            return ResponseEntity.status(403).body("You are not allowed to edit this profile.");
        }

        try {
            UserProfileDTO dto = new UserProfileDTO();
            dto.setUsername(request.getUsername());
            dto.setName(request.getName());
            dto.setSurname(request.getSurname());
            dto.setEmail(request.getEmail());

            UserProfileDTO updated = userProfileService.updateProfile(id, dto);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    // DELETE profile
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProfile(@PathVariable Long id,
                                           Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("You must be logged in to delete profile.");
        }

        String username = authentication.getName();
        User loggedUser = userService.findByUsername(username);
        if (loggedUser == null) {
            return ResponseEntity.status(401).body("Logged user not found.");
        }

        if (!loggedUser.getId().equals(id)) {
            return ResponseEntity.status(403).body("You are not allowed to delete this profile.");
        }

        try {
            userProfileService.deleteProfile(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}