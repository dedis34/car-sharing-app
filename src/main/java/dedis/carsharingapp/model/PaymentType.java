package dedis.carsharingapp.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "payment_typeName")
public class PaymentType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private PaymentTypeName name;

    public enum PaymentTypeName {
        TYPE_PAYMENT,
        TYPE_FINE
    }
}