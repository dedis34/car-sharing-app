package dedis.carsharingapp.model;

public class User {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Role role;

    public enum Role {
        ROLE_MANAGER,
        ROLE_CUSTOMER
    }
}
