package dedis.carsharingapp.repository.payment;

import dedis.carsharingapp.model.PaymentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentTypeRepository extends JpaRepository<PaymentType, Long> {
    Optional<PaymentType> findByName(PaymentType.PaymentTypeName name);
}
