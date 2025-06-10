package dedis.carsharingapp.controller;

public class PaymentsController {
    GET: /payments/?user_id=... - get payments
    POST: /payments/ - create payment session
    GET: /payments/success/ - check successful Stripe payments (Endpoint for stripe redirection)
    GET: /payments/cancel/ - return payment paused message (Endpoint for stripe redirection)
}
