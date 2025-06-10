package dedis.carsharingapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User management", description = "Endpoints for managing users")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/users")
public class UsersController {
    @Operation(summary = "Update user's role by id", description = "Update user's role by his id")
    @PutMapping("/{id}/role")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void updateBookById(@PathVariable Long id, @Valid @RequestBody BookDto bookDto) {
        bookService.updateBookById(id, bookDto);
    }

    @Operation(summary = "Get profile info", description = "Get info about user's profile")
    @GetMapping
    @PutMapping("/me")
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<BookDto> getAll(@Parameter(hidden = true) Pageable pageable) {
        return bookService.findAll(pageable);
    }

    @Operation(summary = "Update user info", description = "Update user's info on profile")
    @PatchMapping("/me")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateOrderStatus(@PathVariable Long id,
                                  @RequestBody @Valid UpdateOrderStatusRequestDto statusUpdateRequestDto) {
        orderServiceImpl.updateOrderStatus(id, statusUpdateRequestDto.status());
    }

}
