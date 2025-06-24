package dedis.carsharingapp.dto.car;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CarRequestDto(
        @NotEmpty(message = "Model is required")
        String model,

        @NotEmpty(message = "Brand is required")
        String brand,

        @Min(value = 0, message = "Inventory must be 0 or greater")
        int inventory,

        @DecimalMin(value = "0.0", inclusive = false, message = "Daily fee must be greater than 0")
        BigDecimal dailyFee,

        @NotNull(message = "Car type ID is required")
        Long carTypeId
) {
}
