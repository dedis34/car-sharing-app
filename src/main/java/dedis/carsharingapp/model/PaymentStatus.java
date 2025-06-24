package dedis.carsharingapp.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "payment_status_name")
public class PaymentStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private StatusName name;

    public enum StatusName {
        STATUS_PENDING,
        STATUS_PAID,
    }
}
