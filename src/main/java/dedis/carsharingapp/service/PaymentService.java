package dedis.carsharingapp.service;

import dedis.carsharingapp.dto.payment.CreatePaymentRequestDto;
import dedis.carsharingapp.dto.payment.PaymentResponseDto;
import dedis.carsharingapp.model.User;

import java.util.List;

public interface PaymentService {
    PaymentResponseDto createPaymentSession(CreatePaymentRequestDto dto, User user);
    void handlePaymentSuccess(String sessionId);
    List<PaymentResponseDto> getPaymentsForUser(Long userId, User currentUser);
    List<PaymentResponseDto> getAllPayments();
}
