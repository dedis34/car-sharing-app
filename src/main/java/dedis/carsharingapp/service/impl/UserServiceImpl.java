package dedis.carsharingapp.service.impl;

import dedis.carsharingapp.dto.user.UserRegistrationRequestDto;
import dedis.carsharingapp.dto.user.UserRegistrationResponseDto;
import dedis.carsharingapp.exceptions.RegistrationException;
import dedis.carsharingapp.mapper.UserMapper;
import dedis.carsharingapp.model.User;
import dedis.carsharingapp.repository.user.UserRepository;
import dedis.carsharingapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserRegistrationResponseDto register(UserRegistrationRequestDto userRegistrationRequestDto) {
        if (userRepository.existsByEmail(userRegistrationRequestDto.email())) {
            throw new RegistrationException("Email already used.");
        }

        User newUser = userMapper.toModel(userRegistrationRequestDto);

        String encodedPassword = passwordEncoder.encode(userRegistrationRequestDto.password());
        newUser.setPassword(encodedPassword);

        userRepository.save(newUser);

        return userMapper.toDto(newUser);
    }
}
