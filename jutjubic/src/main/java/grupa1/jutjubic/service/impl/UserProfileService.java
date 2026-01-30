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

    // GET
    @Override
    public UserProfileDTO getProfileById(Long userId) {
        User user = userProfileRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return mapToDTO(user);
    }

    // UPDATE
    @Override
    public UserProfileDTO updateProfile(Long id, UserProfileDTO dto){
        User user = userProfileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(!user.getId().equals(id)){
            throw new RuntimeException("You are not allowed to edit this profile.");
        }

        user.setName(dto.getName());
        user.setSurname(dto.getSurname());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());

        return mapToDTO(userProfileRepository.save(user));
    }

    // DELETE
    @Override
    public void deleteProfile(Long id){
        User user = userProfileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(!user.getId().equals(id)){
            throw new RuntimeException("You are not allowed to delete this profile.");
        }

        userProfileRepository.delete(user);
    }

    private UserProfileDTO mapToDTO(User user) {
        UserProfileDTO dto = new UserProfileDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setName(user.getName());
        dto.setSurname(user.getSurname());
        dto.setEmail(user.getEmail());
        return dto;
    }
}

