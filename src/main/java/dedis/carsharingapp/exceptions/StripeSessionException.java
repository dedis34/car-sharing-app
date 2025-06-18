package dedis.carsharingapp.exceptions;

public class StripeSessionException extends RuntimeException {
    public StripeSessionException() {
        super();
    }

    public StripeSessionException(String message) {
        super(message);
    }

    public StripeSessionException(String message, Throwable cause) {
        super(message, cause);
    }

    public StripeSessionException(Throwable cause) {
        super(cause);
    }
}
