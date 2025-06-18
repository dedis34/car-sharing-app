package dedis.carsharingapp.dto.rental;

public record SimpleUserDto(
        Long id,
        String firstName,
        String lastName,
        String email
) {
}
