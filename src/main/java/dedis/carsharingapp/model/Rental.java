package dedis.carsharingapp.model;

import java.time.LocalDate;
import lombok.Data;
import jakarta.persistence.*;

@Data
@Entity
@Table(name = "rental")
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(nullable = false)
    private LocalDate date;
    @Column(nullable = false)
    private LocalDate returnDate;
    private LocalDate actualReturnDate;
}
