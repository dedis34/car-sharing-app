package dedis.carsharingapp.exceptions;

public class RentalNotFoundException extends RuntimeException {
    public RentalNotFoundException() {
        super();
    }

    public RentalNotFoundException(String message) {
        super(message);
    }

    public RentalNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public RentalNotFoundException(Throwable cause) {
        super(cause);
    }
}
