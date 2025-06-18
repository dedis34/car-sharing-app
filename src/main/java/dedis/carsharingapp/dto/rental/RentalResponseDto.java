package dedis.carsharingapp.dto.rental;

import java.time.LocalDate;

public record RentalResponseDto(
        Long id,
        SimpleCarDto car,
        SimpleUserDto user,
        LocalDate startDate,
        LocalDate returnDate,
        LocalDate actualReturnDate
) {
}
