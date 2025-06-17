package dedis.carsharingapp.service;

import dedis.carsharingapp.dto.user.UserRegistrationRequestDto;
import dedis.carsharingapp.dto.user.UserRegistrationResponseDto;

public interface UserService {
    UserRegistrationResponseDto register(UserRegistrationRequestDto userRegistrationRequestDto);
}
