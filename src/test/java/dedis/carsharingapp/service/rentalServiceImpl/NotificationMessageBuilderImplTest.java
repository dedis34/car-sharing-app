package dedis.carsharingapp.service.rentalServiceImpl;

import dedis.carsharingapp.model.Car;
import dedis.carsharingapp.model.Rental;
import dedis.carsharingapp.model.User;
import dedis.carsharingapp.service.impl.rentalServiceImpl.NotificationMessageBuilderImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class NotificationMessageBuilderImplTest {

    private NotificationMessageBuilderImpl builder;
    private Rental rental;

    @BeforeEach
    void setUp() {
        builder = new NotificationMessageBuilderImpl();
        User user = new User();
        user.setFirstName("Jan");
        user.setLastName("Kowalski");

        Car car = new Car();
        car.setBrand("Toyota");
        car.setModel("Corolla");

        rental = new Rental();
        rental.setUser(user);
        rental.setCar(car);
        rental.setDate(LocalDate.of(2025, 6, 1));
        rental.setReturnDate(LocalDate.of(2025, 6, 5));
    }

    @Test
    void buildNewRentalMessage_ShouldIncludeAllFields() {
        String msg = builder.buildNewRentalMessage(rental);
        assertTrue(msg.contains("📢 New reservation:"));
        assertTrue(msg.contains("Jan Kowalski"));
        assertTrue(msg.contains("Toyota Corolla"));
        assertTrue(msg.contains("From: 2025-06-01"));
        assertTrue(msg.contains("To: 2025-06-05"));
    }

    @Test
    void buildOverdueRentalMessage_ShouldIncludePlannedReturn() {
        String msg = builder.buildOverdueRentalMessage(rental);
        assertTrue(msg.contains("⚠️ Overdue Rental!"));
        assertTrue(msg.contains("Jan Kowalski"));
        assertTrue(msg.contains("Toyota Corolla"));
        assertTrue(msg.contains("Planned return: 2025-06-05"));
    }
}
