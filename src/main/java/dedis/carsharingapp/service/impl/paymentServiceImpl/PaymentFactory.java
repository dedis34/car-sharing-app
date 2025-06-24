package dedis.carsharingapp.service.impl.paymentServiceImpl;

import com.stripe.model.checkout.Session;
import dedis.carsharingapp.exceptions.PaymentStatusNotFoundException;
import dedis.carsharingapp.exceptions.PaymentTypeNotFoundException;
import dedis.carsharingapp.model.*;
import dedis.carsharingapp.repository.payment.PaymentStatusRepository;
import dedis.carsharingapp.repository.payment.PaymentTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class PaymentFactory {

    private final PaymentStatusRepository paymentStatusRepository;
    private final PaymentTypeRepository paymentTypeRepository;

    public Payment createPayment(Session session, Rental rental, BigDecimal amount, PaymentType.PaymentTypeName type) {
        PaymentStatus status = paymentStatusRepository.findByName(PaymentStatus.StatusName.STATUS_PENDING)
                .orElseThrow(() -> new PaymentStatusNotFoundException("Payment status not found"));

        PaymentType paymentType = paymentTypeRepository.findByName(type)
                .orElseThrow(() -> new PaymentTypeNotFoundException("Payment type not found"));

        Payment payment = new Payment();
        payment.setSessionId(session.getId());
        payment.setSessionUrl(session.getUrl());
        payment.setRental(rental);
        payment.setAmount(amount);
        payment.setStatus(status);
        payment.setType(paymentType);
        return payment;
    }
}

