package dedis.carsharingapp.exceptions;

public class PaymentStatusNotFoundException extends RuntimeException {
    public PaymentStatusNotFoundException(String message) {
        super(message);
    }
}
