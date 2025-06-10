package dedis.carsharingapp.model;

import java.time.LocalDate;

public class Rental {
    private Long id;
    private Long carId;
    private Long userId;
    private LocalDate date;
    private LocalDate returnDate;
    private LocalDate actualReturnDate;
}
