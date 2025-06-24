package dedis.carsharingapp.dto.rental;

import java.math.BigDecimal;

public record SimpleCarDto(
        Long id,
        String brand,
        String model,
        String type,
        BigDecimal dailyFee
) {
}
