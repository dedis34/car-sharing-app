package dedis.carsharingapp.service.paymentServiceImpl;

import com.stripe.model.checkout.Session;
import dedis.carsharingapp.exceptions.PaymentStatusNotFoundException;
import dedis.carsharingapp.exceptions.PaymentTypeNotFoundException;
import dedis.carsharingapp.model.*;
import dedis.carsharingapp.repository.payment.PaymentStatusRepository;
import dedis.carsharingapp.repository.payment.PaymentTypeRepository;
import dedis.carsharingapp.service.impl.paymentServiceImpl.PaymentFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentFactoryTest {

    @Mock
    private PaymentStatusRepository statusRepo;
    @Mock
    private PaymentTypeRepository typeRepo;
    @InjectMocks
    private PaymentFactory factory;

    private Session dummySession;
    private Rental dummyRental;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        dummySession = mock(Session.class);
        when(dummySession.getId()).thenReturn("sess_1");
        when(dummySession.getUrl()).thenReturn("url_1");
        dummyRental = new Rental();
    }

    @Test
    void createPayment_WhenAllFound_ReturnsPayment() {
        PaymentStatus status = new PaymentStatus();
        status.setName(PaymentStatus.StatusName.STATUS_PENDING);
        when(statusRepo.findByName(PaymentStatus.StatusName.STATUS_PENDING)).thenReturn(Optional.of(status));
        PaymentType type = new PaymentType();
        type.setName(PaymentType.PaymentTypeName.TYPE_PAYMENT);
        when(typeRepo.findByName(PaymentType.PaymentTypeName.TYPE_PAYMENT)).thenReturn(Optional.of(type));

        Payment p = factory.createPayment(dummySession, dummyRental, BigDecimal.TEN, PaymentType.PaymentTypeName.TYPE_PAYMENT);

        assertEquals("sess_1", p.getSessionId());
        assertEquals("url_1", p.getSessionUrl());
        assertEquals(dummyRental, p.getRental());
        assertEquals(BigDecimal.TEN, p.getAmount());
        assertEquals(status, p.getStatus());
        assertEquals(type, p.getType());
    }

    @Test
    void createPayment_WhenStatusMissing_Throws() {
        when(statusRepo.findByName(any())).thenReturn(Optional.empty());
        assertThrows(PaymentStatusNotFoundException.class, () ->
                factory.createPayment(dummySession, dummyRental, BigDecimal.ONE, PaymentType.PaymentTypeName.TYPE_PAYMENT));
    }

    @Test
    void createPayment_WhenTypeMissing_Throws() {
        PaymentStatus status = new PaymentStatus();
        when(statusRepo.findByName(any())).thenReturn(Optional.of(status));
        when(typeRepo.findByName(any())).thenReturn(Optional.empty());
        assertThrows(PaymentTypeNotFoundException.class, () ->
                factory.createPayment(dummySession, dummyRental, BigDecimal.ONE, PaymentType.PaymentTypeName.TYPE_FINE));
    }
}
