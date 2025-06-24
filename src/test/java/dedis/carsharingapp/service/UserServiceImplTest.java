package dedis.carsharingapp.service;

import dedis.carsharingapp.dto.user.UpdateUserProfileRequestDto;
import dedis.carsharingapp.dto.user.UpdateUserRoleRequestDto;
import dedis.carsharingapp.dto.user.UserInfoResponseDto;
import dedis.carsharingapp.dto.user.auth.UserRegistrationRequestDto;
import dedis.carsharingapp.dto.user.auth.UserRegistrationResponseDto;
import dedis.carsharingapp.exceptions.*;
import dedis.carsharingapp.mapper.UserMapper;
import dedis.carsharingapp.model.Role;
import dedis.carsharingapp.model.User;
import dedis.carsharingapp.repository.role.RoleRepository;
import dedis.carsharingapp.repository.user.UserRepository;
import dedis.carsharingapp.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock private UserRepository userRepo;
    @Mock private UserMapper mapper;
    @Mock private PasswordEncoder encoder;
    @Mock private RoleRepository roleRepo;

    @InjectMocks private UserServiceImpl service;

    private UserRegistrationRequestDto regReq;
    private User user;
    private UserRegistrationResponseDto regResp;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        regReq = new UserRegistrationRequestDto(
                "Jan", "Nowak", "jan@x.com", "passw0rd", "passw0rd");

        user = new User();
        user.setId(7L);
        user.setFirstName("Jan");
        user.setLastName("Nowak");
        user.setEmail("jan@x.com");

        regResp = new UserRegistrationResponseDto(
                7L, "Jan", "Nowak", "jan@x.com", "pv", Set.of("ROLE_USER"));
    }

    @Test
    void register_WhenNewEmail_ShouldReturnDto() {
        when(userRepo.existsByEmail("jan@x.com")).thenReturn(false);
        when(mapper.toModel(regReq)).thenReturn(user);
        when(encoder.encode("passw0rd")).thenReturn("pv");
        when(userRepo.save(user)).thenReturn(user);
        when(mapper.toDto(user)).thenReturn(regResp);

        var resp = service.register(regReq);
        assertSame(regResp, resp);
    }

    @Test
    void register_WhenEmailExists_ShouldThrow() {
        when(userRepo.existsByEmail("jan@x.com")).thenReturn(true);
        assertThrows(RegistrationException.class, () -> service.register(regReq));
    }

    @Test
    void updateUserRole_WhenValid_ShouldSaveRoles() {
        var roleNames = Set.of(Role.RoleName.ROLE_MANAGER);
        var role = new Role();
        role.setName(Role.RoleName.ROLE_MANAGER);
        var updateReq = new UpdateUserRoleRequestDto(roleNames);

        when(userRepo.findById(5L)).thenReturn(Optional.of(user));
        when(roleRepo.findByName(Role.RoleName.ROLE_MANAGER)).thenReturn(Optional.of(role));

        service.updateUserRole(5L, updateReq);

        assertTrue(user.getRoles().contains(role));
        verify(userRepo).save(user);
    }

    @Test
    void updateUserRole_WhenUserNotFound_ShouldThrow() {
        when(userRepo.findById(5L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class,
                () -> service.updateUserRole(5L, new UpdateUserRoleRequestDto(Set.of())));
    }

    @Test
    void updateUserRole_WhenRoleNotFound_ShouldThrow() {
        when(userRepo.findById(5L)).thenReturn(Optional.of(user));
        when(roleRepo.findByName(any())).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class,
                () -> service.updateUserRole(5L,
                        new UpdateUserRoleRequestDto(Set.of(Role.RoleName.ROLE_CUSTOMER))));
    }

    @Test
    void getUserProfile_WhenExists_ShouldReturnDto() {
        when(userRepo.findByEmail("jan@x.com")).thenReturn(Optional.of(user));
        var info = new UserInfoResponseDto(7L, "Jan", "Nowak", "jan@x.com");
        when(mapper.toUserInfoDto(user)).thenReturn(info);

        var resp = service.getUserProfile("jan@x.com");
        assertSame(info, resp);
    }

    @Test
    void getUserProfile_WhenNotFound_ShouldThrow() {
        when(userRepo.findByEmail("x")).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class,
                () -> service.getUserProfile("x"));
    }

    @Test
    void patchUserProfile_WhenExists_ShouldSavePatched() {
        var patch = new UpdateUserProfileRequestDto("A", "B", "a@b.com");
        when(userRepo.findByEmail("jan@x.com")).thenReturn(Optional.of(user));
        doAnswer(invocation -> {
            user.setFirstName("A");
            return null;
        }).when(mapper).patchUserFromDto(patch, user);

        service.patchUserProfile("jan@x.com", patch);

        verify(userRepo).save(user);
        assertEquals("A", user.getFirstName());
    }

    @Test
    void patchUserProfile_WhenNotFound_ShouldThrow() {
        when(userRepo.findByEmail("x")).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class,
                () -> service.patchUserProfile("x",
                        new UpdateUserProfileRequestDto("", "", "")));
    }
}
