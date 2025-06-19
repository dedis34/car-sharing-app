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

    @Mapping(target = "type", ignore = true)
    Car toModel(CarRequestDto carRequestDto);

    default String map(CarType carType) {
        return carType == null ? null : carType.getName().name();
    }
}


