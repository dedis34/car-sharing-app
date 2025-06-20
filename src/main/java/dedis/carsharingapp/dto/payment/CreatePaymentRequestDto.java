package dedis.carsharingapp.dto.payment;

import dedis.carsharingapp.model.PaymentType;

public record CreatePaymentRequestDto(
        Long rentalId,
        PaymentType.PaymentTypeName paymentType
) {
}
