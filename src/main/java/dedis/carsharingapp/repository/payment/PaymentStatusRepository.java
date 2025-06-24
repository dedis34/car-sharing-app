package dedis.carsharingapp.repository.payment;

import dedis.carsharingapp.model.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentStatusRepository extends JpaRepository<PaymentStatus, Long> {
    Optional<PaymentStatus> findByName(PaymentStatus.StatusName name);

}
