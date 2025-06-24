package dedis.carsharingapp.dto.car;

import java.math.BigDecimal;

public record CarResponseDto(
        Long id,
        String model,
        String brand,
        int inventory,
        BigDecimal dailyFee,
        String carType
) {
}
