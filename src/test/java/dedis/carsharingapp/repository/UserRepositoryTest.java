package dedis.carsharingapp.repository;

import dedis.carsharingapp.config.CustomMySqlContainer;
import dedis.carsharingapp.model.Role;
import dedis.carsharingapp.model.User;
import dedis.carsharingapp.model.Role.RoleName;
import dedis.carsharingapp.repository.role.RoleRepository;
import dedis.carsharingapp.repository.user.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

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
        role.setName(RoleName.ROLE_CUSTOMER);
        role = roleRepository.save(role);

        User user = new User();
        user.setFirstName("Anna");
        user.setLastName("Nowak");
        user.setEmail("anna@example.com");
        user.setPassword("secret123");
        user.setRoles(Set.of(role));

        userRepository.save(user);
    }

    @Test
    void shouldFindUserByEmail() {
        var userOpt = userRepository.findByEmail("anna@example.com");
        assertTrue(userOpt.isPresent());
        assertEquals("Anna", userOpt.get().getFirstName());
    }

    @Test
    void shouldCheckEmailExistence() {
        assertTrue(userRepository.existsByEmail("anna@example.com"));
        assertFalse(userRepository.existsByEmail("ghost@example.com"));
    }

    @AfterAll
    void stopContainer() {
        container.stop();
    }
}
