package dedis.carsharingapp.service;

import dedis.carsharingapp.dto.car.CarRequestDto;
import dedis.carsharingapp.dto.car.CarResponseDto;
import dedis.carsharingapp.exceptions.CarNotFoundException;
import dedis.carsharingapp.exceptions.CarValidationException;
import dedis.carsharingapp.mapper.CarMapper;
import dedis.carsharingapp.model.Car;
import dedis.carsharingapp.model.CarType;
import dedis.carsharingapp.repository.car.CarRepository;
import dedis.carsharingapp.repository.car.CarTypeRepository;
import dedis.carsharingapp.service.impl.CarServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CarServiceImplTest {

    @Mock private CarRepository carRepo;
    @Mock private CarTypeRepository typeRepo;
    @Mock private CarMapper mapper;

    @InjectMocks private CarServiceImpl service;

    private CarRequestDto request;
    private Car car;
    private CarResponseDto response;
    private CarType carType;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        request = new CarRequestDto("ModelX","BrandY",5, BigDecimal.valueOf(100), 42L);
        carType = new CarType(); carType.setId(42L); carType.setName(CarType.TypeName.TYPE_SEDAN);
        car = new Car(); car.setModel("ModelX"); car.setBrand("BrandY"); car.setInventory(5);
        car.setDailyFee(BigDecimal.valueOf(100)); car.setType(carType);
        response = new CarResponseDto(1L,"ModelX","BrandY",5,BigDecimal.valueOf(100),"SEDAN");
    }

    @Test
    void createCar_WhenValid_ShouldReturnDto() {
        when(mapper.toModel(request)).thenReturn(car);
        when(typeRepo.findById(42L)).thenReturn(Optional.of(carType));
        when(carRepo.save(car)).thenReturn(car);
        when(mapper.toDto(car)).thenReturn(response);

        CarResponseDto result = service.createCar(request);

        assertSame(response, result);
    }

    @Test
    void createCar_WhenDailyFeeInvalid_ShouldThrow() {
        var bad = new CarRequestDto("M","B",1, BigDecimal.ZERO, 1L);
        assertThrows(CarValidationException.class, () -> service.createCar(bad));
    }

    @Test
    void createCar_WhenCarTypeMissing_ShouldThrow() {
        when(typeRepo.findById(42L)).thenReturn(Optional.empty());
        assertThrows(CarValidationException.class, () -> service.createCar(request));
    }

    @Test
    void getAllCars_ShouldReturnList() {
        when(carRepo.findAll()).thenReturn(List.of(car));
        when(mapper.toDto(car)).thenReturn(response);

        List<CarResponseDto> list = service.getAllCars();
        assertEquals(1, list.size());
        assertEquals("ModelX", list.get(0).model());
    }

    @Test
    void getCarById_WhenExists_ShouldReturn() {
        when(carRepo.findById(1L)).thenReturn(Optional.of(car));
        when(mapper.toDto(car)).thenReturn(response);

        CarResponseDto dto = service.getCarById(1L);
        assertEquals("BrandY", dto.brand());
    }

    @Test
    void getCarById_WhenNotFound_ShouldThrow() {
        when(carRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(CarNotFoundException.class, () -> service.getCarById(1L));
    }

    @Test
    void updateCar_WhenExists_ShouldReturnUpdated() {
        when(carRepo.findById(1L)).thenReturn(Optional.of(car));
        when(typeRepo.findById(42L)).thenReturn(Optional.of(carType));
        when(carRepo.save(car)).thenReturn(car);
        when(mapper.toDto(car)).thenReturn(response);

        CarResponseDto dto = service.updateCar(1L, request);
        assertEquals("ModelX", dto.model());
    }

    @Test
    void updateCar_WhenNotFound_ShouldThrow() {
        when(carRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(CarNotFoundException.class, () -> service.updateCar(1L, request));
    }

    @Test
    void deleteCar_WhenExists_ShouldMarkDeleted() {
        when(carRepo.findById(1L)).thenReturn(Optional.of(car));
        service.deleteCar(1L);
        assertTrue(car.isDeleted());
        verify(carRepo).save(car);
    }

    @Test
    void deleteCar_WhenNotFound_ShouldThrow() {
        when(carRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(CarNotFoundException.class, () -> service.deleteCar(1L));
    }

    @Test
    void createCar_WhenDailyFeeIsNull_ShouldThrow() {
        var bad = new CarRequestDto("ModelZ", "BrandQ", 3, null, 42L);
        assertThrows(CarValidationException.class, () -> service.createCar(bad));
    }

    @Test
    void createCar_WhenCarTypeIdIsNull_ShouldThrow() {
        var bad = new CarRequestDto("ModelZ", "BrandQ", 3, BigDecimal.TEN, null);
        assertThrows(CarValidationException.class, () -> service.createCar(bad));
    }

}
