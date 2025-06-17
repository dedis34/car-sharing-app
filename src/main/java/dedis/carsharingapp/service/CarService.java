package dedis.carsharingapp.service;

import dedis.carsharingapp.dto.car.CarRequestDto;
import dedis.carsharingapp.dto.car.CarResponseDto;

import java.util.List;

public interface CarService {
    CarResponseDto createCar(CarRequestDto requestDto);
    List<CarResponseDto> getAllCars();
    CarResponseDto getCarById(Long id);
    CarResponseDto updateCar(Long id, CarRequestDto requestDto);
    void deleteCar(Long id);
}
