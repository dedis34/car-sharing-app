package dedis.carsharingapp.service.rentalServiceImpl;

import dedis.carsharingapp.model.Car;
import dedis.carsharingapp.model.Rental;
import dedis.carsharingapp.model.User;
import dedis.carsharingapp.notification.NotificationService;
import dedis.carsharingapp.repository.rental.RentalRepository;
import dedis.carsharingapp.service.impl.rentalServiceImpl.OverdueRentalNotifierServiceImpl;
import dedis.carsharingapp.service.rentalService.NotificationMessageBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

class OverdueRentalNotifierServiceImplTest {

    @Mock private RentalRepository rentalRepo;
    @Mock private NotificationService notificationService;
    @Mock private NotificationMessageBuilder messageBuilder;

    @InjectMocks
    private OverdueRentalNotifierServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void checkAndNotifyOverdueRentals_WhenNone_ShouldSendNoOverdue() {
        when(rentalRepo.findByReturnDateLessThanEqualAndActualReturnDateIsNull(any()))
                .thenReturn(Collections.emptyList());

        service.checkAndNotifyOverdueRentals();

        verify(notificationService).sendMessage("✅ No rentals overdue today!");
        verifyNoMoreInteractions(messageBuilder);
    }

    @Test
    void checkAndNotifyOverdueRentals_WhenSome_ShouldNotifyEach() {
        User u = new User(); u.setFirstName("A"); u.setLastName("B");
        Car c = new Car(); c.setBrand("X"); c.setModel("Y");
        Rental r = new Rental(); r.setUser(u); r.setCar(c); r.setReturnDate(LocalDate.of(2025,6,2));
        List<Rental> list = List.of(r);

        when(rentalRepo.findByReturnDateLessThanEqualAndActualReturnDateIsNull(any()))
                .thenReturn(list);
        when(messageBuilder.buildOverdueRentalMessage(r)).thenReturn("MSG");

        service.checkAndNotifyOverdueRentals();

        verify(notificationService).sendMessage("MSG");
    }
}
