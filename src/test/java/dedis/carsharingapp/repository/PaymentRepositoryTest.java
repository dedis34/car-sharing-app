package dedis.carsharingapp.repository;

import dedis.carsharingapp.config.CustomMySqlContainer;
import dedis.carsharingapp.model.*;
import dedis.carsharingapp.repository.payment.PaymentRepository;
import dedis.carsharingapp.repository.payment.PaymentStatusRepository;
import dedis.carsharingapp.repository.payment.PaymentTypeRepository;
import dedis.carsharingapp.repository.rental.RentalRepository;
import dedis.carsharingapp.repository.user.UserRepository;
import dedis.carsharingapp.repository.car.CarRepository;
import dedis.carsharingapp.repository.car.CarTypeRepository; // <- dodałem import
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
class PaymentRepositoryTest {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private CarTypeRepository carTypeRepository; // <- dodane

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private PaymentStatusRepository statusRepository;

    @Autowired
    private PaymentTypeRepository typeRepository;

    private final CustomMySqlContainer container = CustomMySqlContainer.getInstance();

    private User user;

    @BeforeAll
    void startContainer() {
        container.start();
    }

    @BeforeEach
    void setUp() {
        user = new User();
        user.setFirstName("Eva");
        user.setLastName("Green");
        user.setEmail("eva@pay.com");
        user.setPassword("pass");
        user = userRepository.save(user);

        CarType carType = new CarType();
        carType.setName(CarType.TypeName.TYPE_SEDAN);
        carType = carTypeRepository.save(carType);

        Car car = new Car();
        car.setBrand("Ford");
        car.setModel("Focus");
        car.setDailyFee(BigDecimal.valueOf(100));
        car.setInventory(5);
        car.setType(carType);
        car = carRepository.save(car);


        Rental rental = new Rental();
        rental.setUser(user);
        rental.setCar(car);
        rental.setDate(LocalDate.now());
        rental.setReturnDate(LocalDate.now().plusDays(2));
        rental = rentalRepository.save(rental);

        PaymentStatus status = new PaymentStatus();
        status.setName(PaymentStatus.StatusName.STATUS_PENDING);
        status = statusRepository.save(status);

        PaymentType type = new PaymentType();
        type.setName(PaymentType.PaymentTypeName.TYPE_PAYMENT);
        type = typeRepository.save(type);

        Payment payment = new Payment();
        payment.setRental(rental);
        payment.setSessionId("sess_abc");
        payment.setSessionUrl("http://stripe.com");
        payment.setAmount(BigDecimal.valueOf(49.99));
        payment.setStatus(status);
        payment.setType(type);

        paymentRepository.save(payment);
    }

    @Test
    void shouldFindBySessionId() {
        var result = paymentRepository.findBySessionId("sess_abc");
        assertTrue(result.isPresent());
        assertEquals("sess_abc", result.get().getSessionId());
    }

    @Test
    void shouldFindByRentalUser() {
        List<Payment> result = paymentRepository.findByRentalUser(user);
        assertEquals(1, result.size());
        assertEquals("eva@pay.com", result.get(0).getRental().getUser().getEmail());
    }

    @AfterAll
    void stopContainer() {
        container.stop();
    }
}
