package dedis.carsharingapp.controller;

import dedis.carsharingapp.dto.user.auth.UserLoginRequestDto;
import dedis.carsharingapp.dto.user.auth.UserLoginResponseDto;
import dedis.carsharingapp.dto.user.auth.UserRegistrationRequestDto;
import dedis.carsharingapp.dto.user.auth.UserRegistrationResponseDto;
import dedis.carsharingapp.security.service.AuthenticationService;
import dedis.carsharingapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationControllerTest {

    @InjectMocks
    private AuthenticationController controller;

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationService authenticationService;

    private UserRegistrationRequestDto regReq;
    private UserRegistrationResponseDto regRes;
    private UserLoginRequestDto loginReq;
    private UserLoginResponseDto loginRes;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        regReq = new UserRegistrationRequestDto(
                "Jan", "Kowalski", "jan@example.com",
                "password1", "password1"
        );
        regRes = new UserRegistrationResponseDto(
                42L, "Jan", "Kowalski", "jan@example.com",
                "******", Set.of("ROLE_CUSTOMER")
        );

        loginReq = new UserLoginRequestDto("jan@example.com", "password1");
        loginRes = new UserLoginResponseDto("jwt-token-xyz");
    }

    @Test
    void register_WithValidRequest_ReturnsRegistrationResponse() {
        when(userService.register(regReq)).thenReturn(regRes);

        UserRegistrationResponseDto result = controller.register(regReq);

        assertNotNull(result);
        assertEquals(42L, result.id());
        verify(userService, times(1)).register(regReq);
    }

    @Test
    void login_WithValidCredentials_ReturnsLoginResponse() {
        when(authenticationService.authenticate(loginReq)).thenReturn(loginRes);

        UserLoginResponseDto result = controller.login(loginReq);

        assertNotNull(result);
        assertEquals("jwt-token-xyz", result.token());
        verify(authenticationService, times(1)).authenticate(loginReq);
    }
}
