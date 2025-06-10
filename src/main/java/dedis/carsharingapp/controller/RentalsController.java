package dedis.carsharingapp.controller;

public class RentalsController {
    POST: /rentals - add a new rental (decrease car inventory by 1)
    GET: /rentals/?user_id=...&is_active=... - get rentals by user ID and whether the rental is still active or not
    GET: /rentals/ - get specific rental
    POST: /rentals//return - set actual return date (increase car inventory by 1)
}
