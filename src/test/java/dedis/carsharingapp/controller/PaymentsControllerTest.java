package dedis.carsharingapp.controller;

import dedis.carsharingapp.dto.payment.CreatePaymentRequestDto;
import dedis.carsharingapp.dto.payment.PaymentResponseDto;
import dedis.carsharingapp.model.PaymentType;
import dedis.carsharingapp.model.User;
import dedis.carsharingapp.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentsControllerTest {

    @InjectMocks
    private PaymentsController paymentsController;

    @Mock
    private PaymentService paymentService;

    private User dummyUser;
    private CreatePaymentRequestDto createDto;
    private PaymentResponseDto responseDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        dummyUser = new User();
        dummyUser.setId(42L);
        dummyUser.setEmail("user@example.com");
        createDto = new CreatePaymentRequestDto(99L, PaymentType.PaymentTypeName.TYPE_PAYMENT);
        responseDto = new PaymentResponseDto("https://stripe.checkout/session");
    }

    @Test
    void getPayments_WhenCustomerRequestsTheirPayments_ReturnsListOfPayments() {
        when(paymentService.getPaymentsForUser(42L, dummyUser))
                .thenReturn(Collections.singletonList(responseDto));

        List<PaymentResponseDto> result = paymentsController.getPayments(42L, dummyUser);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("https://stripe.checkout/session", result.get(0).sessionUrl());
        verify(paymentService, times(1)).getPaymentsForUser(42L, dummyUser);
    }

    @Test
    void getAllPayments_WhenManagerRequestsAllPayments_ReturnsListOfPayments() {
        when(paymentService.getAllPayments())
                .thenReturn(Collections.singletonList(responseDto));

        List<PaymentResponseDto> result = paymentsController.getAllPayments();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("https://stripe.checkout/session", result.get(0).sessionUrl());
        verify(paymentService, times(1)).getAllPayments();
    }

    @Test
    void createPaymentSession_WhenValidRequest_ReturnsCreatedSession() {
        when(paymentService.createPaymentSession(createDto, dummyUser))
                .thenReturn(responseDto);

        PaymentResponseDto result = paymentsController.createPaymentSession(createDto, dummyUser);

        assertNotNull(result);
        assertEquals("https://stripe.checkout/session", result.sessionUrl());
        verify(paymentService, times(1)).createPaymentSession(createDto, dummyUser);
    }

    @Test
    void handleStripeSuccess_WithValidSessionId_CallsServiceAndReturnsSuccessMessage() {
        String sessionId = "cs_test_123";
        doNothing().when(paymentService).handlePaymentSuccess(sessionId);

        String message = paymentsController.handleStripeSuccess(sessionId);

        assertEquals("Payment successful. Thank you for your rental!", message);
        verify(paymentService, times(1)).handlePaymentSuccess(sessionId);
    }

    @Test
    void handleStripeCancel_Always_ReturnsCancelMessage() {
        String message = paymentsController.handleStripeCancel();

        assertEquals("Payment was cancelled or failed. Please try again.", message);
        verifyNoInteractions(paymentService);
    }
}
