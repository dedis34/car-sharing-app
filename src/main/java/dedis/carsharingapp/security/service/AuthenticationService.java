package dedis.carsharingapp.security.service;

import dedis.carsharingapp.dto.user.UserLoginRequestDto;
import dedis.carsharingapp.dto.user.UserLoginResponseDto;

public interface AuthenticationService {
    UserLoginResponseDto authenticate(UserLoginRequestDto request);
}
