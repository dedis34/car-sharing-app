package dedis.carsharingapp.controller;

import dedis.carsharingapp.dto.user.UpdateUserProfileRequestDto;
import dedis.carsharingapp.dto.user.UpdateUserRoleRequestDto;
import dedis.carsharingapp.dto.user.UserInfoResponseDto;
import dedis.carsharingapp.model.Role;
import dedis.carsharingapp.model.User;
import dedis.carsharingapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsersControllerTest {

    @InjectMocks
    private UsersController usersController;

    @Mock
    private UserService userService;

    private User dummyUser;
    private UpdateUserRoleRequestDto roleRequest;
    private UpdateUserProfileRequestDto profileRequest;
    private UserInfoResponseDto infoResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        dummyUser = new User();
        dummyUser.setId(7L);
        dummyUser.setEmail("test@example.com");
        dummyUser.setFirstName("Old");
        dummyUser.setLastName("Name");

        roleRequest = new UpdateUserRoleRequestDto(
                Set.of(Role.RoleName.ROLE_MANAGER, Role.RoleName.ROLE_CUSTOMER)
        );

        profileRequest = new UpdateUserProfileRequestDto(
                "NewFirst", "NewLast", "new@example.com"
        );

        infoResponse = new UserInfoResponseDto(
                7L, "Old", "Name", "test@example.com"
        );
    }

    @Test
    void updateUserRole_WithValidRoles_CallsServiceAndReturnsNoContent() {
        doNothing().when(userService).updateUserRole(7L, roleRequest);

        usersController.updateUserRole(7L, roleRequest);

        verify(userService, times(1)).updateUserRole(7L, roleRequest);
    }

    @Test
    void getMyProfile_WithAuthenticatedUser_ReturnsUserInfo() {
        when(userService.getUserProfile("test@example.com")).thenReturn(infoResponse);

        UserInfoResponseDto result = usersController.getMyProfile(dummyUser);

        assertNotNull(result);
        assertEquals(7L, result.id());
        assertEquals("test@example.com", result.email());
        verify(userService, times(1)).getUserProfile("test@example.com");
    }

    @Test
    void patchMyProfile_WithValidProfileRequest_CallsService() {
        doNothing().when(userService).patchUserProfile("test@example.com", profileRequest);

        usersController.patchMyProfile(dummyUser, profileRequest);

        verify(userService, times(1)).patchUserProfile("test@example.com", profileRequest);
    }
}
