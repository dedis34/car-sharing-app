package dedis.carsharingapp.repository;

import dedis.carsharingapp.config.CustomMySqlContainer;
import dedis.carsharingapp.model.PaymentStatus;
import dedis.carsharingapp.model.PaymentStatus.StatusName;
import dedis.carsharingapp.repository.payment.PaymentStatusRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PaymentStatusRepositoryTest {

    @Autowired
    private PaymentStatusRepository repository;

    private final CustomMySqlContainer container = CustomMySqlContainer.getInstance();

    @BeforeAll
    void startContainer() {
        container.start();
    }

    @BeforeEach
    void setUp() {
        PaymentStatus status = new PaymentStatus();
        status.setName(StatusName.STATUS_PENDING);
        repository.save(status);
    }

    @Test
    void shouldFindByName() {
        var result = repository.findByName(StatusName.STATUS_PENDING);
        assertTrue(result.isPresent());
        assertEquals(StatusName.STATUS_PENDING, result.get().getName());
    }

    @AfterAll
    void stopContainer() {
        container.stop();
    }
}
