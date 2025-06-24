package dedis.carsharingapp.service.impl;

import dedis.carsharingapp.dto.car.CarRequestDto;
import dedis.carsharingapp.dto.car.CarResponseDto;
import dedis.carsharingapp.exceptions.CarNotFoundException;
import dedis.carsharingapp.exceptions.CarValidationException;
import dedis.carsharingapp.mapper.CarMapper;
import dedis.carsharingapp.model.Car;
import dedis.carsharingapp.model.CarType;
import dedis.carsharingapp.repository.car.CarRepository;
import dedis.carsharingapp.repository.car.CarTypeRepository;
import dedis.carsharingapp.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;
    private final CarTypeRepository carTypeRepository;
    private final CarMapper carMapper;

    @Override
    public CarResponseDto createCar(CarRequestDto requestDto) {
        validateRequest(requestDto);

        Car car = carMapper.toModel(requestDto);
        car.setType(getCarTypeOrThrow(requestDto.carTypeId()));

        Car saved = carRepository.save(car);
        return carMapper.toDto(saved);
    }

    @Override
    public List<CarResponseDto> getAllCars() {
        return carRepository.findAll().stream()
                .map(carMapper::toDto)
                .toList();
    }

    @Override
    public CarResponseDto getCarById(Long id) {
        return carMapper.toDto(
                carRepository.findById(id)
                        .orElseThrow(() -> new CarNotFoundException("Car with id " + id
                                + " not found"))
        );
    }

    @Override
    public CarResponseDto updateCar(Long id, CarRequestDto requestDto) {
        Car existingCar = carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException("Car with id " + id + " not found"));

        existingCar.setModel(requestDto.model());
        existingCar.setBrand(requestDto.brand());
        existingCar.setInventory(requestDto.inventory());
        existingCar.setDailyFee(requestDto.dailyFee());
        existingCar.setType(getCarTypeOrThrow(requestDto.carTypeId()));

        Car updated = carRepository.save(existingCar);
        return carMapper.toDto(updated);
    }

    @Override
    public void deleteCar(Long id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException("Car with id: " + id + " not found"));
        car.setDeleted(true);
        carRepository.save(car);
    }

    private CarType getCarTypeOrThrow(Long id) {
        return carTypeRepository.findById(id)
                .orElseThrow(() -> new CarValidationException("Car type with id " + id
                        + " not found"));
    }

    private void validateRequest(CarRequestDto requestDto) {
        if (requestDto.dailyFee() == null || requestDto.dailyFee().compareTo(BigDecimal.ZERO)
                <= 0) {
            throw new CarValidationException("Daily fee must be greater than zero");
        }
        if (requestDto.carTypeId() == null) {
            throw new CarValidationException("Car type ID must not be null");
        }
    }
}
