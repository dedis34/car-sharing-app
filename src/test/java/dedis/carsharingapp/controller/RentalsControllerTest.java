package dedis.carsharingapp.controller;

import dedis.carsharingapp.dto.rental.*;
import dedis.carsharingapp.model.User;
import dedis.carsharingapp.service.rentalService.RentalService;
import org.junit.jupiter.api.*;
import org.mockito.*;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RentalsControllerTest {

    @InjectMocks
    private RentalsController controller;

    @Mock
    private RentalService rentalService;

    private CreateRentalRequestDto createReq;
    private RentalResponseDto resp;
    private User mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        createReq = new CreateRentalRequestDto(1L, LocalDate.now(), LocalDate.now().plusDays(3));
        resp = new RentalResponseDto(9L, null, null, LocalDate.now(), LocalDate.now().plusDays(3), null);
        mockUser = new User(); mockUser.setId(7L);
    }

    @Test
    void createRental_WithValidRequest_ReturnsRentalResponse() {
        when(rentalService.createRental(mockUser, createReq)).thenReturn(resp);

        RentalResponseDto result = controller.createRental(mockUser, createReq);

        assertSame(resp, result);
        verify(rentalService).createRental(mockUser, createReq);
    }

    @Test
    void getMyRentals_WithIsActiveParameter_ReturnsList() {
        when(rentalService.getRentalsByUser(mockUser, true)).thenReturn(List.of(resp));

        List<RentalResponseDto> result = controller.getMyRentals(mockUser, true);

        assertEquals(1, result.size());
        verify(rentalService).getRentalsByUser(mockUser, true);
    }

    @Test
    void getRentalById_WithExistingId_ReturnsRental() {
        when(rentalService.getRentalById(5L)).thenReturn(resp);

        RentalResponseDto result = controller.getRentalById(5L);

        assertSame(resp, result);
        verify(rentalService).getRentalById(5L);
    }

    @Test
    void returnRental_WithValidId_CallsService() {
        doNothing().when(rentalService).returnRental(8L);

        controller.returnRental(8L);

        verify(rentalService).returnRental(8L);
    }
}
