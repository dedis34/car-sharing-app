package dedis.carsharingapp.repository;

import dedis.carsharingapp.config.CustomMySqlContainer;
import dedis.carsharingapp.model.Role;
import dedis.carsharingapp.model.Role.RoleName;
import dedis.carsharingapp.repository.role.RoleRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    private final CustomMySqlContainer container = CustomMySqlContainer.getInstance();

    @BeforeAll
    void startContainer() {
        container.start();
    }

    @BeforeEach
    void setUp() {
        Role role = new Role();
        role.setName(RoleName.ROLE_MANAGER);
        roleRepository.save(role);
    }

    @Test
    void shouldFindRoleByName() {
        var found = roleRepository.findByName(RoleName.ROLE_MANAGER);
        assertTrue(found.isPresent());
        assertEquals(RoleName.ROLE_MANAGER, found.get().getName());
    }

    @AfterAll
    void stopContainer() {
        container.stop();
    }
}
