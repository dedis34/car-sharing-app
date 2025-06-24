package dedis.carsharingapp.service.impl;

import dedis.carsharingapp.dto.user.UpdateUserProfileRequestDto;
import dedis.carsharingapp.dto.user.UpdateUserRoleRequestDto;
import dedis.carsharingapp.dto.user.UserInfoResponseDto;
import dedis.carsharingapp.dto.user.auth.UserRegistrationRequestDto;
import dedis.carsharingapp.dto.user.auth.UserRegistrationResponseDto;
import dedis.carsharingapp.exceptions.RegistrationException;
import dedis.carsharingapp.exceptions.RoleNotFoundException;
import dedis.carsharingapp.exceptions.UserNotFoundException;
import dedis.carsharingapp.mapper.UserMapper;
import dedis.carsharingapp.model.Role;
import dedis.carsharingapp.model.User;
import dedis.carsharingapp.repository.role.RoleRepository;
import dedis.carsharingapp.repository.user.UserRepository;
import dedis.carsharingapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public UserRegistrationResponseDto
    register(UserRegistrationRequestDto userRegistrationRequestDto) {
        if (userRepository.existsByEmail(userRegistrationRequestDto.email())) {
            throw new RegistrationException("Email already used.");
        }

        User newUser = userMapper.toModel(userRegistrationRequestDto);

        String encodedPassword = passwordEncoder.encode(userRegistrationRequestDto.password());
        newUser.setPassword(encodedPassword);

        userRepository.save(newUser);

        return userMapper.toDto(newUser);
    }

    @Override
    public void updateUserRole(Long userId, UpdateUserRoleRequestDto request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        Set<Role> roles = request.roles().stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RoleNotFoundException("Role not found: "
                                + roleName)))
                .collect(Collectors.toSet());
        user.setRoles(roles);
        userRepository.save(user);
    }

    @Override
    public UserInfoResponseDto getUserProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: "
                        + email));

        return userMapper.toUserInfoDto(user);
    }

    @Override
    public void patchUserProfile(String email, UpdateUserProfileRequestDto request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: "
                        + email));

        userMapper.patchUserFromDto(request, user);
        userRepository.save(user);
    }
}
