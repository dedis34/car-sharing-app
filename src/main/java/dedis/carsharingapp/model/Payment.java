package dedis.carsharingapp.model;

import java.math.BigDecimal;

public class Payment {
    private Long id;
    private Long rentalId;
    private String sessionId;
    private String sessionUrl;
    private BigDecimal amount;
    private Status status;
    private Type type;

    public enum Status {
        STATUS_PENDING,
        STATUS_PAID,
    }

    public enum Type {
        TYPE_PAYMENT,
        TYPE_FINE
    }
}
