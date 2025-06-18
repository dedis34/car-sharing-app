package dedis.carsharingapp.exceptions;

public class NoAvailableCarsException extends RuntimeException {
    public NoAvailableCarsException() {
        super();
    }

    public NoAvailableCarsException(String message) {
        super(message);
    }

    public NoAvailableCarsException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoAvailableCarsException(Throwable cause) {
        super(cause);
    }
}
