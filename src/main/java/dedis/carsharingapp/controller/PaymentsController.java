package dedis.carsharingapp.controller;

import dedis.carsharingapp.dto.payment.CreatePaymentRequestDto;
import dedis.carsharingapp.dto.payment.PaymentResponseDto;
import dedis.carsharingapp.model.User;
import dedis.carsharingapp.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Payments", description = "Endpoints for managing Stripe payments")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentsController {

    private final PaymentService paymentService;

    @Operation(
            summary = "Get payments",
            description = "Retrieves all payments for a specific user. "
                    + "Only accessible by the MANAGER or the user themselves."
    )
    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<PaymentResponseDto> getPayments(@RequestParam(name = "user_id") Long userId,
                                                @AuthenticationPrincipal User currentUser) {
        return paymentService.getPaymentsForUser(userId, currentUser);
    }

    @Operation(
            summary = "Create payment session",
            description = "Creates a new Stripe payment session for a rental."
    )
    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentResponseDto createPaymentSession(@Valid @RequestBody CreatePaymentRequestDto request,
                                                   @AuthenticationPrincipal User user) {
        return paymentService.createPaymentSession(request, user);
    }

    @Operation(
            summary = "Stripe success redirect",
            description = "Handles the redirection after successful Stripe payment"
    )
    @GetMapping("/success")
    public String handleStripeSuccess() {
        return "Payment successful. Thank you for your rental!";
    }

    @Operation(
            summary = "Stripe cancel redirect",
            description = "Handles the redirection after payment cancellation or failure"
    )
    @GetMapping("/cancel")
    public String handleStripeCancel() {
        return "Payment was cancelled or failed. Please try again.";
    }
}
