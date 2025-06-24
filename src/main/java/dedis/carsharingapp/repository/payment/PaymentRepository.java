package dedis.carsharingapp.repository.payment;

import dedis.carsharingapp.model.Payment;
import dedis.carsharingapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findBySessionId(String sessionId);
    List<Payment> findByRentalUser(User user);
}

