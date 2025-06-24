package dedis.carsharingapp.service.impl.rentalServiceImpl;

import dedis.carsharingapp.notification.NotificationService;
import dedis.carsharingapp.repository.rental.RentalRepository;
import dedis.carsharingapp.service.rentalService.NotificationMessageBuilder;
import dedis.carsharingapp.service.rentalService.OverdueRentalNotifierService;
import dedis.carsharingapp.model.Rental;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OverdueRentalNotifierServiceImpl implements OverdueRentalNotifierService {

    private final RentalRepository rentalRepository;
    private final NotificationService notificationService;
    private final NotificationMessageBuilder notificationMessageBuilder;

    @Scheduled(cron = "0 0 8 * * *")
    @Override
    public void checkAndNotifyOverdueRentals() {
        LocalDate today = LocalDate.now();

        List<Rental> overdueRentals = rentalRepository
                .findByReturnDateLessThanEqualAndActualReturnDateIsNull(today);

        if (overdueRentals.isEmpty()) {
            notificationService.sendMessage("✅ No rentals overdue today!");
            log.info("No overdue rentals found.");
            return;
        }

        for (Rental rental : overdueRentals) {
            String message = notificationMessageBuilder.buildOverdueRentalMessage(rental);
            notificationService.sendMessage(message);
        }

        log.info("Sent {} overdue rental notifications.", overdueRentals.size());
    }
}
