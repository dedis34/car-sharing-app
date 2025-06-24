package dedis.carsharingapp.exceptions;

public class WrongDateProvidedException extends RuntimeException {
    public WrongDateProvidedException(String message) {
        super(message);
    }
}
