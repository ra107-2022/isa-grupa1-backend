package grupa1.jutjubic.controller;

import grupa1.jutjubic.dto.UserProfileDTO;
import grupa1.jutjubic.service.IUserProfileService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserProfileController {

    private final IUserProfileService userProfileService;

    public UserProfileController(IUserProfileService userProfileService){
        this.userProfileService = userProfileService;
    }

    //GET profile
    @GetMapping("/{id}")
    public UserProfileDTO getUserProfile(@PathVariable Long id) {
        return userProfileService.getProfileById(id);
    }
}
