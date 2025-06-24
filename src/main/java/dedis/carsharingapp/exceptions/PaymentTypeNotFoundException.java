package dedis.carsharingapp.exceptions;

public class PaymentTypeNotFoundException extends RuntimeException {
    public PaymentTypeNotFoundException(String message) {
        super(message);
    }
}
