package dedis.carsharingapp.dto.user;

public record UserInfoResponseDto(
        Long id,
        String firstName,
        String lastName,
        String email
) {
}