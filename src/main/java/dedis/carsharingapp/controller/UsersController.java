package dedis.carsharingapp.controller;

import dedis.carsharingapp.dto.user.UpdateUserProfileRequestDto;
import dedis.carsharingapp.dto.user.UpdateUserRoleRequestDto;
import dedis.carsharingapp.dto.user.UserInfoResponseDto;
import dedis.carsharingapp.model.User;
import dedis.carsharingapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User management", description = "Endpoints for managing users")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UsersController {

    private final UserService userService;

    @Operation(summary = "Update user's role by id", description = "Update user's role by their id")
    @PutMapping("/{id}/role")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUserRole(@PathVariable Long id,
                               @Valid @RequestBody UpdateUserRoleRequestDto request) {
        userService.updateUserRole(id, request);
    }

    @Operation(summary = "Get profile info", description = "Get info about logged-in "
            + "user's profile")
    @GetMapping("/me")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public UserInfoResponseDto getMyProfile(@AuthenticationPrincipal User user) {
        return userService.getUserProfile(user.getEmail());
    }

    @Operation(summary = "Partially update user info", description = "Partially update user's profile info")
    @PatchMapping("/me")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void patchMyProfile(@AuthenticationPrincipal User user,
                               @Valid @RequestBody UpdateUserProfileRequestDto request) {
        userService.patchUserProfile(user.getEmail(), request);
    }
}
