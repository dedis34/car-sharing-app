package dedis.carsharingapp.exceptions;

public class PaymentStatusNotFoundException extends RuntimeException {
    public PaymentStatusNotFoundException() {
        super();
    }

    public PaymentStatusNotFoundException(String message) {
        super(message);
    }

    public PaymentStatusNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public PaymentStatusNotFoundException(Throwable cause) {
        super(cause);
    }
}
