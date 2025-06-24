package dedis.carsharingapp.service.paymentServiceImpl;

import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import dedis.carsharingapp.service.impl.paymentServiceImpl.StripeSessionCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StripeSessionCreatorTest {

    @InjectMocks
    private StripeSessionCreator creator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(creator, "successUrl", "https://app/success");
        ReflectionTestUtils.setField(creator, "cancelUrl", "https://app/cancel");
    }

    @Test
    void createSession_BuildsCorrectParamsAndReturnsSession() throws Exception {
        BigDecimal amount = BigDecimal.valueOf(12.34);
        String itemName = "ITEM";

        var paramsCaptor = ArgumentCaptor.forClass(SessionCreateParams.class);
        try (var sc = mockStatic(Session.class)) {
            Session dummy = mock(Session.class);
            sc.when(() -> Session.create(paramsCaptor.capture())).thenReturn(dummy);

            Session result = creator.createSession(amount, itemName);
            assertSame(dummy, result);

            SessionCreateParams p = paramsCaptor.getValue();
            assertEquals("https://app/success?session_id={CHECKOUT_SESSION_ID}", p.getSuccessUrl());
            assertEquals("https://app/cancel", p.getCancelUrl());
            assertEquals(1, p.getLineItems().size());
            var li = p.getLineItems().get(0);
            assertEquals(1L, li.getQuantity());
            long expectedCents = amount.multiply(BigDecimal.valueOf(100)).longValue();
            assertEquals(expectedCents, li.getPriceData().getUnitAmount());
            assertEquals(itemName, li.getPriceData().getProductData().getName());
        }
    }
}
