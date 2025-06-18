package dedis.carsharingapp.mapper;

import dedis.carsharingapp.dto.payment.PaymentResponseDto;
import dedis.carsharingapp.model.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface PaymentMapper {

    @Mapping(source = "sessionUrl", target = "sessionUrl")
    PaymentResponseDto toDto(Payment payment);
}
