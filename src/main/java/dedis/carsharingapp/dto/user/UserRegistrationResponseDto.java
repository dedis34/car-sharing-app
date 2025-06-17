package dedis.carsharingapp.dto.user;

import java.util.Set;

public record UserRegistrationResponseDto(
        Long id,
        String firstName,
        String lastName,
        String email,
        String password,
        Set<String> roles) {
}
