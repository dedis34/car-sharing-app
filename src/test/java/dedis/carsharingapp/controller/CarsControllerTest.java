package dedis.carsharingapp.controller;

import dedis.carsharingapp.dto.car.CarRequestDto;
import dedis.carsharingapp.dto.car.CarResponseDto;
import dedis.carsharingapp.service.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CarsControllerTest {

    @InjectMocks
    private CarsController controller;

    @Mock
    private CarService carService;

    private CarRequestDto requestDto;
    private CarResponseDto responseDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        requestDto = new CarRequestDto(
                "Civic", "Honda", 5, BigDecimal.valueOf(49.99), 1L
        );
        responseDto = new CarResponseDto(
                1L, "Civic", "Honda", 5, BigDecimal.valueOf(49.99), "SEDAN"
        );
    }

    @Test
    void addCar_WithValidRequest_ReturnsCreatedCar() {
        when(carService.createCar(requestDto)).thenReturn(responseDto);

        CarResponseDto result = controller.addCar(requestDto);

        assertNotNull(result);
        assertEquals("Honda", result.brand());
        verify(carService, times(1)).createCar(requestDto);
    }

    @Test
    void getAllCars_ReturnsListOfCars() {
        when(carService.getAllCars()).thenReturn(List.of(responseDto));

        List<CarResponseDto> result = controller.getAllCars();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(carService, times(1)).getAllCars();
    }

    @Test
    void getCarById_WithExistingId_ReturnsCar() {
        when(carService.getCarById(1L)).thenReturn(responseDto);

        CarResponseDto result = controller.getCarById(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
        verify(carService, times(1)).getCarById(1L);
    }

    @Test
    void updateCar_WithValidRequest_ReturnsUpdatedCar() {
        when(carService.updateCar(1L, requestDto)).thenReturn(responseDto);

        CarResponseDto result = controller.updateCar(1L, requestDto);

        assertNotNull(result);
        verify(carService, times(1)).updateCar(1L, requestDto);
    }

    @Test
    void deleteCar_WithValidId_CallsServiceDelete() {
        doNothing().when(carService).deleteCar(1L);

        controller.deleteCar(1L);

        verify(carService, times(1)).deleteCar(1L);
    }
}
