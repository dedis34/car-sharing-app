package dedis.carsharingapp.service;

import dedis.carsharingapp.model.User;
import dedis.carsharingapp.dto.rental.CreateRentalRequestDto;
import dedis.carsharingapp.dto.rental.RentalResponseDto;
import java.util.List;

public interface RentalService {
    RentalResponseDto createRental(User user, CreateRentalRequestDto request);
    List<RentalResponseDto> getRentalsByUser(User user, Boolean isActive);
    RentalResponseDto getRentalById(Long id);
    void returnRental(Long rentalId);
}
