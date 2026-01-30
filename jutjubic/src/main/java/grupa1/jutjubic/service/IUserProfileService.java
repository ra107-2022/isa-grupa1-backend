package grupa1.jutjubic.service;

import grupa1.jutjubic.dto.UserProfileDTO;

public interface IUserProfileService {
    UserProfileDTO getProfileById(Long id);
    UserProfileDTO updateProfile(Long id, UserProfileDTO dto);
    void deleteProfile(Long id);
}
