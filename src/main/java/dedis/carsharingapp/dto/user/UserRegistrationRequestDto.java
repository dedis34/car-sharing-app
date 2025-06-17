package dedis.carsharingapp.dto.user;

import dedis.carsharingapp.customAnnnotations.FieldMatch;
import dedis.carsharingapp.customAnnnotations.UniqueEmail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@FieldMatch(field = "password", fieldMatch = "repeatPassword", message = "Passwords must match")
public record UserRegistrationRequestDto(
        @NotEmpty(message = "First name is required")
        String firstName,

        @NotEmpty(message = "Last name is required")
        String lastName,

        @Email(message = "Email should be valid")
        @NotEmpty(message = "Email is required")
        @Size(min = 8, max = 35)
        @UniqueEmail
        String email,

        @NotEmpty(message = "Password is required")
        @Size(min = 6, max = 20, message = "Password must be at least 6 characters")
        String password,

        @NotEmpty(message = "Password is required")
        @Size(min = 6, max = 20, message = "Password and repeatPassword must match")
        String repeatPassword) {
}
