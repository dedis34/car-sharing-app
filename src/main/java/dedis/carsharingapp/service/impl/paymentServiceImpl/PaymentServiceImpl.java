package dedis.carsharingapp.service.impl.paymentServiceImpl;

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
import dedis.carsharingapp.service.PaymentService;
import lombok.RequiredArgsConstructor;
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
    private final PaymentMapper paymentMapper;
    private final UserRepository userRepository;
    private final PaymentFactory paymentFactory;
    private final PaymentAmountCalculator paymentAmountCalculator;
    private final StripeSessionCreator stripeSessionCreator;

    @Override
    public PaymentResponseDto createPaymentSession(CreatePaymentRequestDto dto, User user) {
        Rental rental = rentalRepository.findById(dto.rentalId())
                .orElseThrow(() -> new RentalNotFoundException("Rental not found"));

        if (!rental.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You do not have permission to create a payment for this rental");
        }

        BigDecimal amount = paymentAmountCalculator.calculateAmount(rental, dto.paymentType());

        try {
            Session session = stripeSessionCreator.createSession(amount,
                    dto.paymentType().name() + " - " + rental.getCar().getModel());

            Payment payment = paymentFactory.createPayment(session, rental, amount, dto.paymentType());
            paymentRepository.save(payment);

            return paymentMapper.toDto(payment);
        } catch (StripeException e) {
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

    @Override
    public List<PaymentResponseDto> getAllPayments() {
        List<Payment> payments = paymentRepository.findAll();
        return payments.stream()
                .map(paymentMapper::toDto)
                .collect(Collectors.toList());
    }

}
