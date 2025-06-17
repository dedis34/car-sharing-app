package dedis.carsharingapp.exceptions;

public class CarNotFoundException extends RuntimeException {
    public CarNotFoundException() {
        super();
    }

    public CarNotFoundException(String message) {
        super(message);
    }

    public CarNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CarNotFoundException(Throwable cause) {
        super(cause);
    }
}
