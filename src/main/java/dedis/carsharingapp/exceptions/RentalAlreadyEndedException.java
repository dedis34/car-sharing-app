package dedis.carsharingapp.exceptions;

public class RentalAlreadyEndedException extends RuntimeException {
    public RentalAlreadyEndedException() {
        super();
    }

    public RentalAlreadyEndedException(String message) {
        super(message);
    }

    public RentalAlreadyEndedException(String message, Throwable cause) {
        super(message, cause);
    }

    public RentalAlreadyEndedException(Throwable cause) {
        super(cause);
    }
}
