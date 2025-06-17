package dedis.carsharingapp.controller;

import dedis.carsharingapp.dto.car.CarRequestDto;
import dedis.carsharingapp.dto.car.CarResponseDto;
import dedis.carsharingapp.service.CarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Cars", description = "Endpoints for managing cars")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/cars")
public class CarsController {

    private final CarService carService;

    @Operation(
            summary = "Add a new car",
            description = "Adds a new car to the system. Only accessible by users with "
                    + "the MANAGER role."
    )
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CarResponseDto addCar(@RequestBody @Valid CarRequestDto requestDto) {
        return carService.createCar(requestDto);
    }

    @Operation(
            summary = "Get all cars",
            description = "Retrieves a list of all available cars. "
                    + "Accessible by MANAGER and CUSTOMER roles."
    )
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @GetMapping
    public List<CarResponseDto> getAllCars() {
        return carService.getAllCars();
    }

    @Operation(
            summary = "Get car details by ID",
            description = "Fetches detailed information about a specific car using its ID."
                    + " Accessible by MANAGER and CUSTOMER roles."
    )
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @GetMapping("/{id}")
    public CarResponseDto getCarById(@PathVariable Long id) {
        return carService.getCarById(id);
    }

    @Operation(
            summary = "Update car by ID",
            description = "Updates the information of an existing car by its ID. "
                    + "Only accessible by users with the MANAGER role."
    )
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PutMapping("/{id}")
    public CarResponseDto updateCar(@PathVariable Long id,
                                    @RequestBody @Valid CarRequestDto requestDto) {
        return carService.updateCar(id, requestDto);
    }

    @Operation(
            summary = "Delete car by ID",
            description = "Deletes a car from the system using its ID. "
                    + "Only accessible by users with the MANAGER role."
    )
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);
    }

}
