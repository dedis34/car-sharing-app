package dedis.carsharingapp.service.paymentServiceImpl;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import dedis.carsharingapp.dto.payment.CreatePaymentRequestDto;
import dedis.carsharingapp.dto.payment.PaymentResponseDto;
import dedis.carsharingapp.exceptions.*;
import dedis.carsharingapp.mapper.PaymentMapper;
import dedis.carsharingapp.model.*;
import dedis.carsharingapp.repository.payment.PaymentRepository;
import dedis.carsharingapp.repository.payment.PaymentStatusRepository;
import dedis.carsharingapp.repository.rental.RentalRepository;
import dedis.carsharingapp.repository.user.UserRepository;
import dedis.carsharingapp.service.impl.paymentServiceImpl.PaymentAmountCalculator;
import dedis.carsharingapp.service.impl.paymentServiceImpl.PaymentFactory;
import dedis.carsharingapp.service.impl.paymentServiceImpl.PaymentServiceImpl;
import dedis.carsharingapp.service.impl.paymentServiceImpl.StripeSessionCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.access.AccessDeniedException;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentServiceImplTest {

    @Mock private PaymentRepository paymentRepo;
    @Mock private RentalRepository rentalRepo;
    @Mock private PaymentStatusRepository statusRepo;
    @Mock private PaymentMapper mapper;
    @Mock private UserRepository userRepo;
    @Mock private PaymentFactory factory;
    @Mock private PaymentAmountCalculator calculator;
    @Mock private StripeSessionCreator stripeSessionCreator;

    @InjectMocks
    private PaymentServiceImpl service;

    private User user;
    private Rental rental;
    private CreatePaymentRequestDto dto;
    private Session session;
    private Payment payment;
    private PaymentResponseDto resp;

    @BeforeEach
    void setUp() throws StripeException {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        Role customerRole = new Role();
        customerRole.setId(1L);
        customerRole.setName(Role.RoleName.ROLE_CUSTOMER);
        user.setRoles(Set.of(customerRole));

        Car car = new Car();
        car.setModel("Tesla");
        car.setDailyFee(BigDecimal.TEN);

        rental = new Rental();
        rental.setId(5L);
        rental.setUser(user);
        rental.setCar(car);

        dto = new CreatePaymentRequestDto(5L, PaymentType.PaymentTypeName.TYPE_PAYMENT);

        session = mock(Session.class);
        when(session.getId()).thenReturn("s1");
        when(session.getUrl()).thenReturn("u1");

        payment = new Payment();
        resp = new PaymentResponseDto("u1");

        when(rentalRepo.findById(5L)).thenReturn(Optional.of(rental));
        when(calculator.calculateAmount(rental, dto.paymentType())).thenReturn(BigDecimal.TEN);
        when(stripeSessionCreator.createSession(any(), any())).thenReturn(session);
        when(factory.createPayment(any(), any(), any(), any())).thenReturn(payment);
        when(paymentRepo.save(any())).thenReturn(payment);
        when(mapper.toDto(payment)).thenReturn(resp);
    }

    @Test
    void createPaymentSession_WhenSuccessful_ReturnsDto() {
        PaymentResponseDto result = service.createPaymentSession(dto, user);
        assertSame(resp, result);
        verify(paymentRepo).save(payment);
    }

    @Test
    void createPaymentSession_WhenRentalNotFound_ThrowsRentalNotFoundException() {
        when(rentalRepo.findById(5L)).thenReturn(Optional.empty());
        assertThrows(RentalNotFoundException.class,
                () -> service.createPaymentSession(dto, user));
    }

    @Test
    void createPaymentSession_WhenNotOwner_ThrowsAccessDeniedException() {
        User otherUser = new User();
        otherUser.setId(2L);
        assertThrows(AccessDeniedException.class,
                () -> service.createPaymentSession(dto, otherUser));
    }

    @Test
    void createPaymentSession_WhenStripeFails_ThrowsStripeSessionException() throws StripeException {
        doThrow(new StripeException("fail", null, null, 0, null) {})
                .when(stripeSessionCreator).createSession(any(), any());

        assertThrows(StripeSessionException.class,
                () -> service.createPaymentSession(dto, user));
    }

    @Test
    void handlePaymentSuccess_WhenSuccessful_UpdatesStatus() {
        Payment paymentMock = mock(Payment.class);
        PaymentStatus paidStatus = new PaymentStatus();
        paidStatus.setName(PaymentStatus.StatusName.STATUS_PAID);

        when(paymentRepo.findBySessionId("s1")).thenReturn(Optional.of(paymentMock));
        when(statusRepo.findByName(PaymentStatus.StatusName.STATUS_PAID)).thenReturn(Optional.of(paidStatus));

        service.handlePaymentSuccess("s1");

        verify(paymentMock).setStatus(paidStatus);
        verify(paymentRepo).save(paymentMock);
    }

    @Test
    void handlePaymentSuccess_WhenPaymentNotFound_ThrowsException() {
        when(paymentRepo.findBySessionId("bad")).thenReturn(Optional.empty());
        assertThrows(PaymentNotFoundException.class,
                () -> service.handlePaymentSuccess("bad"));
    }

    @Test
    void handlePaymentSuccess_WhenStatusNotFound_ThrowsException() {
        when(paymentRepo.findBySessionId("s1")).thenReturn(Optional.of(payment));
        when(statusRepo.findByName(PaymentStatus.StatusName.STATUS_PAID)).thenReturn(Optional.empty());
        assertThrows(PaymentStatusNotFoundException.class,
                () -> service.handlePaymentSuccess("s1"));
    }

    @Test
    void getPaymentsForUser_WhenManager_CanAccessOthersPayments() {
        User manager = new User();
        manager.setId(99L);
        Role role = new Role();
        role.setName(Role.RoleName.ROLE_MANAGER);
        manager.setRoles(Set.of(role));

        List<Payment> payments = List.of(payment);
        when(userRepo.findById(user.getId())).thenReturn(Optional.of(user));
        when(paymentRepo.findByRentalUser(user)).thenReturn(payments);
        when(mapper.toDto(any())).thenReturn(resp);

        List<PaymentResponseDto> result = service.getPaymentsForUser(user.getId(), manager);
        assertEquals(1, result.size());
        assertEquals(resp, result.get(0));
    }

    @Test
    void getPaymentsForUser_WhenSameUser_CanAccessOwnPayments() {
        when(userRepo.findById(user.getId())).thenReturn(Optional.of(user));
        when(paymentRepo.findByRentalUser(user)).thenReturn(List.of(payment));
        when(mapper.toDto(payment)).thenReturn(resp);

        List<PaymentResponseDto> result = service.getPaymentsForUser(user.getId(), user);
        assertEquals(1, result.size());
    }

    @Test
    void getPaymentsForUser_WhenNotAuthorized_ThrowsAccessDenied() {
        User targetUser = new User();
        targetUser.setId(2L);

        User currentUser = new User();
        currentUser.setId(1L);
        Role role = new Role();
        role.setName(Role.RoleName.ROLE_CUSTOMER);
        currentUser.setRoles(Set.of(role));

        when(userRepo.findById(2L)).thenReturn(Optional.of(targetUser));

        assertThrows(AccessDeniedException.class,
                () -> service.getPaymentsForUser(2L, currentUser));
    }

    @Test
    void getAllPayments_ReturnsAllMapped() {
        when(paymentRepo.findAll()).thenReturn(List.of(payment));
        when(mapper.toDto(payment)).thenReturn(resp);

        List<PaymentResponseDto> result = service.getAllPayments();
        assertEquals(1, result.size());
        assertEquals(resp, result.get(0));
    }
}
