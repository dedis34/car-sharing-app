package dedis.carsharingapp.dto.user.auth;

import java.util.Set;

public record UserRegistrationResponseDto(
        Long id,
        String firstName,
        String lastName,
        String email,
        String password,
        Set<String> roles) {
}
