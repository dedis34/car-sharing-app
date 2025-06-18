package dedis.carsharingapp.exceptions;

public class WrongDateProvidedException extends RuntimeException {
    public WrongDateProvidedException() {
        super();
    }

    public WrongDateProvidedException(String message) {
        super(message);
    }

    public WrongDateProvidedException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongDateProvidedException(Throwable cause) {
        super(cause);
    }
}
