package dedis.carsharingapp.exceptions;

public class PaymentTypeNotFoundException extends RuntimeException {
    public PaymentTypeNotFoundException() {
        super();
    }

    public PaymentTypeNotFoundException(String message) {
        super(message);
    }

    public PaymentTypeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public PaymentTypeNotFoundException(Throwable cause) {
        super(cause);
    }
}
