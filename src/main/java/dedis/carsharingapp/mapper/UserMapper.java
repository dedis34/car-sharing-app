package dedis.carsharingapp.mapper;

import dedis.carsharingapp.config.MapperConfig;
import dedis.carsharingapp.dto.user.UserRegistrationRequestDto;
import dedis.carsharingapp.dto.user.UserRegistrationResponseDto;
import dedis.carsharingapp.model.Role;
import dedis.carsharingapp.model.User;
import org.mapstruct.Mapper;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserRegistrationResponseDto toDto(User user);
    User toModel(UserRegistrationRequestDto userRegistrationRequestDto);

    default Set<String> mapRolesToRoleNames(Set<Role> roles) {
        return roles.stream()
                .map(role -> role.getName().toString())
                .collect(Collectors.toSet());
    }
}


