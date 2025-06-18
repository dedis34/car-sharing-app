package dedis.carsharingapp.security.service;

import dedis.carsharingapp.dto.user.auth.UserLoginRequestDto;
import dedis.carsharingapp.dto.user.auth.UserLoginResponseDto;

public interface AuthenticationService {
    UserLoginResponseDto authenticate(UserLoginRequestDto request);
}
