package dedis.carsharingapp.exceptions;

public class RentalAlreadyEndedException extends RuntimeException {
    public RentalAlreadyEndedException(String message) {
        super(message);
    }
}
