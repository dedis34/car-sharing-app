package dedis.carsharingapp.repository.rental;

import dedis.carsharingapp.model.Rental;
import dedis.carsharingapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface RentalRepository  extends JpaRepository<Rental, Long> {
    List<Rental> findByUser(User user);
    List<Rental> findByUserAndActualReturnDateIsNull(User user);
    List<Rental> findByUserAndActualReturnDateIsNotNull(User user);
    List<Rental> findByReturnDateLessThanEqualAndActualReturnDateIsNull(LocalDate date);

}
