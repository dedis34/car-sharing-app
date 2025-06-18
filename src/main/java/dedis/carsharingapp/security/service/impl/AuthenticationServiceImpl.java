package dedis.carsharingapp.security.service.impl;

import lombok.RequiredArgsConstructor;
import dedis.carsharingapp.dto.user.auth.UserLoginRequestDto;
import dedis.carsharingapp.dto.user.auth.UserLoginResponseDto;
import dedis.carsharingapp.security.service.AuthenticationService;
import dedis.carsharingapp.security.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    public UserLoginResponseDto authenticate(UserLoginRequestDto request) {

        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        String token = jwtUtil.generateToken(authenticate.getName());

        return new UserLoginResponseDto(token);
    }
}

