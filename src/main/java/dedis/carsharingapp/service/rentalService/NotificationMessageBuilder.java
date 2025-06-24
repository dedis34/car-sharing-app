package dedis.carsharingapp.service.rentalService;

import dedis.carsharingapp.model.Rental;

public interface NotificationMessageBuilder {
    String buildNewRentalMessage(Rental rental);
    String buildOverdueRentalMessage(Rental rental);
}
