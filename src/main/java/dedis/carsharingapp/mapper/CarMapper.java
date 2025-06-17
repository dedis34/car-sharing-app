package dedis.carsharingapp.mapper;

import dedis.carsharingapp.config.MapperConfig;
import dedis.carsharingapp.dto.car.CarRequestDto;
import dedis.carsharingapp.dto.car.CarResponseDto;
import dedis.carsharingapp.model.Car;
import dedis.carsharingapp.model.CarType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface CarMapper {
    @Mapping(source = "type", target = "carType")
    CarResponseDto toDto(Car car);

    Car toModel(CarRequestDto carRequestDto);

    default String mapCarTypeToString(CarType type) {
        return type == null ? null : type.getName().name();
    }
}

