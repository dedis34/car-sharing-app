package dedis.carsharingapp.service.impl.paymentServiceImpl;

import dedis.carsharingapp.model.PaymentType;
import dedis.carsharingapp.model.Rental;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
public class PaymentAmountCalculator {

    @Value("${fine.multiplier}")
    private BigDecimal fineMultiplier;

    public BigDecimal calculateAmount(Rental rental, PaymentType.PaymentTypeName type) {
        BigDecimal baseFee = rental.getCar().getDailyFee();

        if (type == PaymentType.PaymentTypeName.TYPE_FINE) {
            long overdueDays = ChronoUnit.DAYS.between(rental.getReturnDate(), LocalDate.now());
            overdueDays = Math.max(0, overdueDays);
            return baseFee.multiply(BigDecimal.valueOf(overdueDays)).multiply(fineMultiplier);
        }

        return baseFee;
    }
}
