package dedis.carsharingapp.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "car_type")
public class CarType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private TypeName name;

    public enum TypeName {
        TYPE_SEDAN,
        TYPE_SUV,
        TYPE_HATCHBACK,
        TYPE_UNIVERSAL
    }
}
