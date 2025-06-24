package dedis.carsharingapp.dto.user;

import jakarta.validation.constraints.NotEmpty;

public record UpdateUserProfileRequestDto(
        @NotEmpty(message = "First name cannot be empty")
        String firstName,

        @NotEmpty(message = "Last name cannot be empty")
        String lastName,

        @NotEmpty(message = "Email cannot be empty")
        String email
) {
}
