package dedis.carsharingapp.model;

import java.math.BigDecimal;

public class Car {
    private Long id;
    private String model;
    private String brand;
    private int inventory;
    private BigDecimal dailyFee;
    private Type type;

    public enum Type {
        TYPE_SEDAN,
        TYPE_SUV,
        TYPE_HATCHBACK,
        TYPE_UNIVERSAL,
    }
}
