package dedis.carsharingapp.mapper;

import dedis.carsharingapp.config.MapperConfig;
import dedis.carsharingapp.dto.rental.*;
import dedis.carsharingapp.model.Rental;
import org.mapstruct.*;

@Mapper(config = MapperConfig.class, uses = {CarMapper.class, UserMapper.class})
public interface RentalMapper {

    @Mapping(source = "car", target = "car")
    @Mapping(source = "user", target = "user")
    RentalResponseDto toDto(Rental rental);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "actualReturnDate", ignore = true)
    @Mapping(source = "carId", target = "car.id")
    @Mapping(target = "user", ignore = true)
    @Mapping(source = "returnDate", target = "returnDate")
    @Mapping(target = "date", expression = "java(java.time.LocalDate.now())")
    Rental toModel(CreateRentalRequestDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRentalFromDto(CreateRentalRequestDto dto, @MappingTarget Rental rental);

}
