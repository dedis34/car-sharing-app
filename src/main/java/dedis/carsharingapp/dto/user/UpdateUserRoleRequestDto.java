package dedis.carsharingapp.dto.user;

import dedis.carsharingapp.model.Role;
import jakarta.validation.constraints.NotEmpty;
import java.util.Set;

public record UpdateUserRoleRequestDto(
        @NotEmpty
        Set<Role.RoleName> roles
) {}

