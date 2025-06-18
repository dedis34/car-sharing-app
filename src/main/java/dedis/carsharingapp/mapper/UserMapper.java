package dedis.carsharingapp.mapper;

import dedis.carsharingapp.config.MapperConfig;
import dedis.carsharingapp.dto.user.UpdateUserProfileRequestDto;
import dedis.carsharingapp.dto.user.UserInfoResponseDto;
import dedis.carsharingapp.dto.user.auth.UserRegistrationRequestDto;
import dedis.carsharingapp.dto.user.auth.UserRegistrationResponseDto;
import dedis.carsharingapp.model.Role;
import dedis.carsharingapp.model.User;
import org.mapstruct.*;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    @Mapping(source = "roles", target = "roles")
    UserRegistrationResponseDto toDto(User user);

    User toModel(UserRegistrationRequestDto userRegistrationRequestDto);

    UserInfoResponseDto toUserInfoDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patchUserFromDto(UpdateUserProfileRequestDto dto, @MappingTarget User user);

    default Set<String> mapRolesToRoleNames(Set<Role> roles) {
        return roles.stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet());
    }
}



