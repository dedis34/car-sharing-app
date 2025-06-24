package dedis.carsharingapp.service.rentalServiceImpl;

import dedis.carsharingapp.dto.rental.CreateRentalRequestDto;
import dedis.carsharingapp.dto.rental.RentalResponseDto;
import dedis.carsharingapp.dto.rental.SimpleCarDto;
import dedis.carsharingapp.dto.rental.SimpleUserDto;
import dedis.carsharingapp.exceptions.*;
import dedis.carsharingapp.mapper.RentalMapper;
import dedis.carsharingapp.model.Car;
import dedis.carsharingapp.model.Rental;
import dedis.carsharingapp.model.User;
import dedis.carsharingapp.notification.NotificationService;
import dedis.carsharingapp.repository.car.CarRepository;
import dedis.carsharingapp.repository.rental.RentalRepository;
import dedis.carsharingapp.service.impl.rentalServiceImpl.RentalServiceImpl;
import dedis.carsharingapp.service.rentalService.NotificationMessageBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RentalServiceImplTest {

    @Mock private RentalRepository rentalRepo;
    @Mock private CarRepository carRepo;
    @Mock private RentalMapper mapper;
    @Mock private NotificationService notif;
    @Mock private NotificationMessageBuilder msgBuilder;

    @InjectMocks
    private RentalServiceImpl service;

    private User user;
    private Car car;
    private CreateRentalRequestDto req;
    private Rental rental;
    private RentalResponseDto dto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(10L);
        user.setFirstName("Jan");
        user.setLastName("Nowak");
        user.setEmail("jan@x.com");

        car = new Car();
        car.setId(20L);
        car.setBrand("Toyota");
        car.setModel("Corolla");
        car.setDailyFee(BigDecimal.valueOf(50));
        car.setInventory(2);

        req = new CreateRentalRequestDto(
                car.getId(),
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(3)
        );

        rental = new Rental();
        rental.setId(100L);
        rental.setUser(user);
        rental.setCar(car);
        rental.setDate(req.startDate());
        rental.setReturnDate(req.returnDate());

        SimpleCarDto sc = new SimpleCarDto(car.getId(), car.getBrand(), car.getModel(), "SEDAN", car.getDailyFee());
        SimpleUserDto su = new SimpleUserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());

        dto = new RentalResponseDto(
                rental.getId(), sc, su, rental.getDate(), rental.getReturnDate(), null
        );
    }

    @Test
    void createRental_WhenValid_ShouldSaveAndNotify() {
        when(carRepo.findById(car.getId())).thenReturn(Optional.of(car));
        when(mapper.toModel(req)).thenReturn(rental);
        when(rentalRepo.save(rental)).thenReturn(rental);
        when(mapper.toDto(rental)).thenReturn(dto);
        when(msgBuilder.buildNewRentalMessage(rental)).thenReturn("MSG");

        RentalResponseDto result = service.createRental(user, req);

        assertSame(dto, result);
        assertEquals(1, car.getInventory());
        verify(notif).sendMessage("MSG");
    }

    @Test
    void createRental_WhenReturnBeforeStart_ShouldThrowWrongDate() {
        CreateRentalRequestDto bad = new CreateRentalRequestDto(
                car.getId(),
                LocalDate.now().plusDays(5),
                LocalDate.now().plusDays(2)
        );
        assertThrows(WrongDateProvidedException.class, () -> service.createRental(user, bad));
    }

    @Test
    void createRental_WhenCarNotFound_ShouldThrow() {
        when(carRepo.findById(any())).thenReturn(Optional.empty());
        assertThrows(CarNotFoundException.class, () -> service.createRental(user, req));
    }

    @Test
    void createRental_WhenNoInventory_ShouldThrow() {
        car.setInventory(0);
        when(carRepo.findById(car.getId())).thenReturn(Optional.of(car));
        assertThrows(NoAvailableCarsException.class, () -> service.createRental(user, req));
    }

    @Test
    void returnRental_WhenValid_ShouldUpdateInventoryAndDate() {
        when(rentalRepo.findById(100L)).thenReturn(Optional.of(rental));

        service.returnRental(100L);

        assertNotNull(rental.getActualReturnDate());
        assertEquals(3, car.getInventory());
        verify(rentalRepo).save(rental);
        verify(carRepo).save(car);
    }

    @Test
    void returnRental_WhenNotFound_ShouldThrow() {
        when(rentalRepo.findById(100L)).thenReturn(Optional.empty());
        assertThrows(RentalNotFoundException.class, () -> service.returnRental(100L));
    }

    @Test
    void returnRental_WhenAlreadyReturned_ShouldThrow() {
        rental.setActualReturnDate(LocalDate.now());
        when(rentalRepo.findById(100L)).thenReturn(Optional.of(rental));
        assertThrows(RentalAlreadyEndedException.class, () -> service.returnRental(100L));
    }

    @Test
    void getRentalsByUser_WhenIsActiveIsNull_ShouldReturnAllRentals() {
        List<Rental> rentalList = List.of(rental);
        when(rentalRepo.findByUser(user)).thenReturn(rentalList);
        when(mapper.toDto(rental)).thenReturn(dto);

        List<RentalResponseDto> result = service.getRentalsByUser(user, null);

        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
    }

    @Test
    void getRentalsByUser_WhenIsActiveTrue_ShouldReturnActiveRentals() {
        List<Rental> rentalList = List.of(rental);
        when(rentalRepo.findByUserAndActualReturnDateIsNull(user)).thenReturn(rentalList);
        when(mapper.toDto(rental)).thenReturn(dto);

        List<RentalResponseDto> result = service.getRentalsByUser(user, true);

        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
    }

    @Test
    void getRentalsByUser_WhenIsActiveFalse_ShouldReturnReturnedRentals() {
        List<Rental> rentalList = List.of(rental);
        when(rentalRepo.findByUserAndActualReturnDateIsNotNull(user)).thenReturn(rentalList);
        when(mapper.toDto(rental)).thenReturn(dto);

        List<RentalResponseDto> result = service.getRentalsByUser(user, false);

        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
    }

    @Test
    void getRentalById_WhenExists_ShouldReturnDto() {
        when(rentalRepo.findById(100L)).thenReturn(Optional.of(rental));
        when(mapper.toDto(rental)).thenReturn(dto);

        RentalResponseDto result = service.getRentalById(100L);

        assertEquals(dto, result);
    }

    @Test
    void getRentalById_WhenNotExists_ShouldThrow() {
        when(rentalRepo.findById(100L)).thenReturn(Optional.empty());
        assertThrows(RentalNotFoundException.class, () -> service.getRentalById(100L));
    }

    @Test
    void getOverdueRentals_ShouldReturnList() {
        LocalDate today = LocalDate.now();
        List<Rental> overdueRentals = List.of(rental);
        when(rentalRepo.findByReturnDateLessThanEqualAndActualReturnDateIsNull(today)).thenReturn(overdueRentals);

        List<Rental> result = service.getOverdueRentals(today);

        assertEquals(1, result.size());
        assertEquals(rental, result.get(0));
    }
}
