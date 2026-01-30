package grupa1.jutjubic.service;

import grupa1.jutjubic.dto.UserProfileDTO;

public interface IUserProfileService {
    UserProfileDTO getProfileById(Long id);
}
