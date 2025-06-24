package dedis.carsharingapp.service.impl.rentalServiceImpl;

import dedis.carsharingapp.dto.rental.CreateRentalRequestDto;
import dedis.carsharingapp.dto.rental.RentalResponseDto;
import dedis.carsharingapp.exceptions.*;
import dedis.carsharingapp.mapper.RentalMapper;
import dedis.carsharingapp.model.Car;
import dedis.carsharingapp.model.Rental;
import dedis.carsharingapp.model.User;
import dedis.carsharingapp.notification.NotificationService;
import dedis.carsharingapp.repository.car.CarRepository;
import dedis.carsharingapp.repository.rental.RentalRepository;
import dedis.carsharingapp.service.rentalService.RentalService;
import dedis.carsharingapp.service.rentalService.NotificationMessageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RentalServiceImpl implements RentalService {

    private final RentalRepository rentalRepository;
    private final CarRepository carRepository;
    private final RentalMapper rentalMapper;
    private final NotificationService notificationService;
    private final NotificationMessageBuilder notificationMessageBuilder;

    @Override
    @Transactional
    public RentalResponseDto createRental(User user, CreateRentalRequestDto request) {
        if (request.returnDate().isBefore(request.startDate())) {
            throw new WrongDateProvidedException("Return date must be after start date");
        }

        Car car = carRepository.findById(request.carId())
                .orElseThrow(() -> new CarNotFoundException("Car not found with id: " + request.carId()));

        if (car.getInventory() <= 0) {
            throw new NoAvailableCarsException("No cars available for model: " + car.getModel());
        }

        car.setInventory(car.getInventory() - 1);
        carRepository.save(car);

        Rental rental = rentalMapper.toModel(request);
        rental.setUser(user);
        rental.setCar(car);
        rental.setDate(request.startDate());
        rental.setReturnDate(request.returnDate());

        Rental savedRental = rentalRepository.save(rental);

        String message = notificationMessageBuilder.buildNewRentalMessage(savedRental);
        notificationService.sendMessage(message);

        return rentalMapper.toDto(savedRental);
    }


    @Override
    public List<RentalResponseDto> getRentalsByUser(User user, Boolean isActive) {
        List<Rental> rentals;
        if (isActive == null) {
            rentals = rentalRepository.findByUser(user);
        } else if (isActive) {
            rentals = rentalRepository.findByUserAndActualReturnDateIsNull(user);
        } else {
            rentals = rentalRepository.findByUserAndActualReturnDateIsNotNull(user);
        }
        return rentals.stream()
                .map(rentalMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public RentalResponseDto getRentalById(Long id) {
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new RentalNotFoundException("Rental not found with id: " + id));
        return rentalMapper.toDto(rental);
    }

    @Override
    @Transactional
    public void returnRental(Long rentalId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new RentalNotFoundException("Rental not found with id: " + rentalId));

        if (rental.getActualReturnDate() != null) {
            throw new RentalAlreadyEndedException("Rental has already been returned.");
        }

        rental.setActualReturnDate(LocalDate.now());

        Car car = rental.getCar();
        car.setInventory(car.getInventory() + 1);
        carRepository.save(car);

        rentalRepository.save(rental);
    }

    @Override
    public List<Rental> getOverdueRentals(LocalDate date) {
        return rentalRepository.findByReturnDateLessThanEqualAndActualReturnDateIsNull(date);
    }
}
