package dedis.carsharingapp.repository;

import dedis.carsharingapp.config.CustomMySqlContainer;
import dedis.carsharingapp.model.*;
import dedis.carsharingapp.repository.car.CarRepository;
import dedis.carsharingapp.repository.car.CarTypeRepository;
import dedis.carsharingapp.repository.rental.RentalRepository;
import dedis.carsharingapp.repository.user.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RentalRepositoryTest {

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private CarTypeRepository carTypeRepository;

    private final CustomMySqlContainer container = CustomMySqlContainer.getInstance();

    private User user;

    @BeforeAll
    void startContainer() {
        container.start();
    }

    @BeforeEach
    void setUp() {
        user = new User();
        user.setFirstName("Tom");
        user.setLastName("Ford");
        user.setEmail("tom@cars.com");
        user.setPassword("pass");
        user = userRepository.save(user);

        CarType type = new CarType();
        type.setName(CarType.TypeName.TYPE_SEDAN);
        type = carTypeRepository.save(type);

        Car car = new Car();
        car.setModel("Yaris");
        car.setBrand("Toyota");
        car.setInventory(10);
        car.setDailyFee(BigDecimal.valueOf(100));
        car.setType(type);
        car = carRepository.save(car);

        Rental rental = new Rental();
        rental.setUser(user);
        rental.setCar(car);
        rental.setDate(LocalDate.now());
        rental.setReturnDate(LocalDate.now().plusDays(3));
        rental.setActualReturnDate(null);
        rentalRepository.save(rental);
    }

    @Test
    void shouldFindByUser() {
        List<Rental> result = rentalRepository.findByUser(user);
        assertEquals(1, result.size());
    }

    @Test
    void shouldFindActiveRentals() {
        List<Rental> result = rentalRepository.findByUserAndActualReturnDateIsNull(user);
        assertEquals(1, result.size());
    }

    @AfterAll
    void stopContainer() {
        container.stop();
    }
}
