package dedis.carsharingapp.exceptions;

public class CarValidationException extends RuntimeException {
    public CarValidationException(String message) {
        super(message);
    }
}
