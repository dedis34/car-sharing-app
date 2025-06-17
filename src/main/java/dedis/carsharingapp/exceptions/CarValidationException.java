package dedis.carsharingapp.exceptions;

public class CarValidationException extends RuntimeException {
    public CarValidationException() {
        super();
    }

    public CarValidationException(String message) {
        super(message);
    }

    public CarValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public CarValidationException(Throwable cause) {
        super(cause);
    }
}
