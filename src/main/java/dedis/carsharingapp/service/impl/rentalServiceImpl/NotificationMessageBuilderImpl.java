package dedis.carsharingapp.service.impl.rentalServiceImpl;

import dedis.carsharingapp.model.Rental;
import dedis.carsharingapp.service.rentalService.NotificationMessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class NotificationMessageBuilderImpl implements NotificationMessageBuilder {

    @Override
    public String buildNewRentalMessage(Rental rental) {
        return String.format(
                "📢 New reservation:\n👤 %s %s\n🚗 %s %s\n📅 From: %s To: %s",
                rental.getUser().getFirstName(),
                rental.getUser().getLastName(),
                rental.getCar().getBrand(),
                rental.getCar().getModel(),
                rental.getDate(),
                rental.getReturnDate()
        );
    }

    @Override
    public String buildOverdueRentalMessage(Rental rental) {
        return String.format(
                "⚠️ Overdue Rental!\n👤 %s %s\n🚗 %s %s\n📅 Planned return: %s",
                rental.getUser().getFirstName(),
                rental.getUser().getLastName(),
                rental.getCar().getBrand(),
                rental.getCar().getModel(),
                rental.getReturnDate()
        );
    }
}
