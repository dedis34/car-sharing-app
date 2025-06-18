package dedis.carsharingapp.service.impl;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import dedis.carsharingapp.dto.payment.CreatePaymentRequestDto;
import dedis.carsharingapp.dto.payment.PaymentResponseDto;
import dedis.carsharingapp.exceptions.*;
import dedis.carsharingapp.mapper.PaymentMapper;
import dedis.carsharingapp.model.*;
import dedis.carsharingapp.repository.payment.PaymentRepository;
import dedis.carsharingapp.repository.payment.PaymentStatusRepository;
import dedis.carsharingapp.repository.payment.PaymentTypeRepository;
import dedis.carsharingapp.repository.rental.RentalRepository;
import dedis.carsharingapp.repository.user.UserRepository;
import dedis.carsharingapp.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final RentalRepository rentalRepository;
    private final PaymentStatusRepository paymentStatusRepository;
    private final PaymentTypeRepository paymentTypeRepository;
    private final PaymentMapper paymentMapper;
    private final UserRepository userRepository;

    @Value("${stripe.success-url}")
    private String successUrl;

    @Value("${stripe.cancel-url}")
    private String cancelUrl;

    @Override
    public PaymentResponseDto createPaymentSession(CreatePaymentRequestDto dto, User user) {
        Rental rental = rentalRepository.findById(dto.rentalId())
                .orElseThrow(() -> new RentalNotFoundException("Rental not found"));

        long amountInCents = rental.getCar().getDailyFee()
                .multiply(BigDecimal.valueOf(100)).longValue();

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(successUrl + "?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl(cancelUrl)
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("usd")
                                                .setUnitAmount(amountInCents)
                                                .setProductData(
                                                        SessionCreateParams
                                                                .LineItem.PriceData
                                                                .ProductData.builder()
                                                                .setName("Rental - " + rental
                                                                        .getCar()
                                                                        .getModel())
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                )
                .build();

        try {
            Session session = Session.create(params);

            Payment payment = new Payment();
            payment.setSessionId(session.getId());
            payment.setSessionUrl(session.getUrl());
            payment.setRental(rental);
            payment.setAmount(rental.getCar().getDailyFee());
            payment.setStatus(paymentStatusRepository.findByName(PaymentStatus
                            .StatusName.STATUS_PENDING)
                    .orElseThrow(() -> new PaymentStatusNotFoundException
                            ("Payment status not found")));
            payment.setType(paymentTypeRepository.findByName(PaymentType
                            .PaymentTypeName.TYPE_PAYMENT)
                    .orElseThrow(() -> new PaymentTypeNotFoundException
                            ("Payment type not found")));
            paymentRepository.save(payment);

            return paymentMapper.toDto(payment);
        }
        catch (StripeException e) {
            throw new StripeSessionException("Stripe session error: " + e.getMessage());
        }
    }

    @Override
    public void handlePaymentSuccess(String sessionId) {
        Payment payment = paymentRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found"));
        PaymentStatus paidStatus = paymentStatusRepository.findByName(PaymentStatus
                        .StatusName.STATUS_PAID)
                .orElseThrow(() -> new PaymentStatusNotFoundException("Payment status not found"));
        payment.setStatus(paidStatus);
        paymentRepository.save(payment);
    }

    @Override
    public List<PaymentResponseDto> getPaymentsForUser(Long userId, User currentUser) {
        User requestedUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isManager = currentUser.getRoles().stream()
                .anyMatch(role -> role.getName() == Role.RoleName.ROLE_MANAGER);
        if (!isManager && !currentUser.getId().equals(userId)) {
            throw new AccessDeniedException("You do not have permission to view these payments");
        }

        List<Payment> payments = paymentRepository.findByRentalUser(requestedUser);

        return payments.stream()
                .map(paymentMapper::toDto)
                .collect(Collectors.toList());
    }
}
