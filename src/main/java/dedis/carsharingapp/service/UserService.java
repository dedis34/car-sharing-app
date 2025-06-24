package dedis.carsharingapp.service;

import dedis.carsharingapp.dto.user.UpdateUserProfileRequestDto;
import dedis.carsharingapp.dto.user.UpdateUserRoleRequestDto;
import dedis.carsharingapp.dto.user.UserInfoResponseDto;
import dedis.carsharingapp.dto.user.auth.UserRegistrationRequestDto;
import dedis.carsharingapp.dto.user.auth.UserRegistrationResponseDto;

public interface UserService {
    UserRegistrationResponseDto register(UserRegistrationRequestDto userRegistrationRequestDto);
    void updateUserRole(Long userId, UpdateUserRoleRequestDto request);
    UserInfoResponseDto getUserProfile(String email);
    void patchUserProfile(String email, UpdateUserProfileRequestDto request);
}
