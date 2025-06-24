package dedis.carsharingapp.controller;

import dedis.carsharingapp.dto.rental.CreateRentalRequestDto;
import dedis.carsharingapp.dto.rental.RentalResponseDto;
import dedis.carsharingapp.model.User;
import dedis.carsharingapp.service.rentalService.RentalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Rental management", description = "Endpoints for managing car rentals")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rentals")
public class RentalsController {

    private final RentalService rentalService;

    @Operation(summary = "Create new rental", description = "Create a new rental and "
            + "decrease car inventory by 1")
    @PostMapping
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @ResponseStatus(HttpStatus.CREATED)
    public RentalResponseDto createRental(@AuthenticationPrincipal User user,
                                          @Valid @RequestBody CreateRentalRequestDto request) {
        return rentalService.createRental(user, request);
    }

    @Operation(summary = "Get rentals", description = "Get rentals by user and rental status")
    @GetMapping
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public List<RentalResponseDto> getMyRentals(@AuthenticationPrincipal User user,
                                                @RequestParam(name = "is_active", required = false)
                                                Boolean isActive) {
        return rentalService.getRentalsByUser(user, isActive);
    }

    @Operation(summary = "Get rental by ID", description = "Get details of a specific rental")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public RentalResponseDto getRentalById(@PathVariable Long id) {
        return rentalService.getRentalById(id);
    }

    @Operation(summary = "Return car", description = "Set actual return startDate and "
            + "increase car inventory by 1")
    @PostMapping("/{id}/return")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void returnRental(@PathVariable Long id) {
        rentalService.returnRental(id);
    }
}
