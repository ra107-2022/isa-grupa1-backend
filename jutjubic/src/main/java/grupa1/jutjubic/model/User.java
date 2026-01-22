package grupa1.jutjubic.model;

import jakarta.persistence.*;

@Entity
@Table(name="USERS")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", unique = false, nullable = false)
    private String password;

    public User() {
        super();
    }

    public User(String username, String password)
    {
        super();
        this.username = username;
        this.password = password;
    }
}
