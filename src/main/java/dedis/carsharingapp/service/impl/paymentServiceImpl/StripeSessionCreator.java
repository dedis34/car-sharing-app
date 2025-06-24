package dedis.carsharingapp.service.impl.paymentServiceImpl;

import lombok.RequiredArgsConstructor;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import java.math.BigDecimal;


@Component
@RequiredArgsConstructor
public class StripeSessionCreator {

    @Value("${stripe.success-url}")
    private String successUrl;

    @Value("${stripe.cancel-url}")
    private String cancelUrl;

    public Session createSession(BigDecimal totalAmount, String itemName) throws StripeException {
        long amountInCents = totalAmount.multiply(BigDecimal.valueOf(100)).longValue();

        String fullSuccessUrl = UriComponentsBuilder
                .fromUriString(successUrl)
                .queryParam("session_id", "{CHECKOUT_SESSION_ID}")
                .build()
                .toUriString();

        String fullCancelUrl = UriComponentsBuilder
                .fromUriString(cancelUrl)
                .build()
                .toUriString();

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(fullSuccessUrl)
                .setCancelUrl(fullCancelUrl)
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("usd")
                                                .setUnitAmount(amountInCents)
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName(itemName)
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                )
                .build();

        return Session.create(params);
    }
}
