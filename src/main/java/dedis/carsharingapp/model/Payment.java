package dedis.carsharingapp.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rental_id", nullable = false)
    private Rental rental;
    @Column(nullable = false)
    private String sessionId;
    @Column(nullable = false)
    private String sessionUrl;
    @Column(nullable = false)
    private BigDecimal amount;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "paymentStatus_id", nullable = false)
    private PaymentStatus status;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "paymentType_id", nullable = false)
    private PaymentType type;

}
