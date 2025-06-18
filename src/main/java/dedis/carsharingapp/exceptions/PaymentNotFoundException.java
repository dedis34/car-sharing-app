package dedis.carsharingapp.exceptions;

public class PaymentNotFoundException extends RuntimeException {
    public PaymentNotFoundException() {
        super();
    }

    public PaymentNotFoundException(String message) {
        super(message);
    }

    public PaymentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public PaymentNotFoundException(Throwable cause) {
        super(cause);
    }
}
