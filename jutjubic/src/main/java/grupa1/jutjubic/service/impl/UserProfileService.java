package grupa1.jutjubic.service.impl;

import grupa1.jutjubic.service.IUserProfileService;
import grupa1.jutjubic.dto.UserProfileDTO;
import grupa1.jutjubic.model.User;
import org.springframework.stereotype.Service;
import grupa1.jutjubic.repository.UserProfileRepository;

@Service
public class UserProfileService implements IUserProfileService {

    private final UserProfileRepository userProfileRepository;

    public UserProfileService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public UserProfileDTO getProfileById(Long id) {
        User user = userProfileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserProfileDTO dto = new UserProfileDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setName(user.getName());
        dto.setSurname(user.getSurname());
        dto.setEmail(user.getEmail());

        return dto;
    }
}

