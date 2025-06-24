package dedis.carsharingapp.service.paymentServiceImpl;

import dedis.carsharingapp.model.Car;
import dedis.carsharingapp.model.Rental;
import dedis.carsharingapp.model.PaymentType;
import dedis.carsharingapp.service.impl.paymentServiceImpl.PaymentAmountCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PaymentAmountCalculatorTest {

    @InjectMocks
    private PaymentAmountCalculator calculator;

    private Rental rental;
    private static final BigDecimal DAILY_FEE = BigDecimal.valueOf(50);
    private static final BigDecimal MULTIPLIER = BigDecimal.valueOf(1.5);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        calculator = new PaymentAmountCalculator();
        ReflectionTestUtils.setField(calculator, "fineMultiplier", MULTIPLIER);

        Car car = new Car();
        car.setDailyFee(DAILY_FEE);
        rental = new Rental();
        rental.setCar(car);
    }

    @Test
    void calculateAmount_WhenTypePayment_ReturnsBaseFee() {
        BigDecimal result = calculator.calculateAmount(rental, PaymentType.PaymentTypeName.TYPE_PAYMENT);
        assertEquals(DAILY_FEE, result);
    }

    @Test
    void calculateAmount_WhenTypeFineAndNoOverdue_ReturnsZero() {
        rental.setReturnDate(LocalDate.now().plusDays(1));
        BigDecimal wynik = calculator.calculateAmount(rental, PaymentType.PaymentTypeName.TYPE_FINE);
        assertEquals(
                0,
                wynik.compareTo(BigDecimal.ZERO),
                "Require 0, when there is no day overdue"
        );
    }


    @Test
    void calculateAmount_WhenTypeFineAndOneDayOverdue_ReturnsFeeTimesMultiplier() {
        rental.setReturnDate(LocalDate.now().minusDays(1));
        BigDecimal expected = DAILY_FEE.multiply(BigDecimal.valueOf(1)).multiply(MULTIPLIER);
        BigDecimal result = calculator.calculateAmount(rental, PaymentType.PaymentTypeName.TYPE_FINE);
        assertEquals(0, expected.compareTo(result));
    }

    @Test
    void calculateAmount_WhenTypeFineAndMultipleDaysOverdue_ReturnsCorrect() {
        rental.setReturnDate(LocalDate.now().minusDays(3));
        BigDecimal expected = DAILY_FEE.multiply(BigDecimal.valueOf(3)).multiply(MULTIPLIER);
        BigDecimal result = calculator.calculateAmount(rental, PaymentType.PaymentTypeName.TYPE_FINE);
        assertEquals(0, expected.compareTo(result));
    }
}
