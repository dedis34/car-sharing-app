package dedis.carsharingapp.repository;

import dedis.carsharingapp.config.CustomMySqlContainer;
import dedis.carsharingapp.model.PaymentType;
import dedis.carsharingapp.model.PaymentType.PaymentTypeName;
import dedis.carsharingapp.repository.payment.PaymentTypeRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PaymentTypeRepositoryTest {

    @Autowired
    private PaymentTypeRepository repository;

    private final CustomMySqlContainer container = CustomMySqlContainer.getInstance();

    @BeforeAll
    void startContainer() {
        container.start();
    }

    @BeforeEach
    void setUp() {
        PaymentType type = new PaymentType();
        type.setName(PaymentTypeName.TYPE_PAYMENT);
        repository.save(type);
    }

    @Test
    void shouldFindByName() {
        var result = repository.findByName(PaymentTypeName.TYPE_PAYMENT);
        assertTrue(result.isPresent());
        assertEquals(PaymentTypeName.TYPE_PAYMENT, result.get().getName());
    }

    @AfterAll
    void stopContainer() {
        container.stop();
    }
}
