package org.example.avtodiller.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "users")
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "id")
    private Long id;
    @Column(name = "login", unique = true)
    private String login;
    @Column(name = "password")
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private RoleModel role;

    public UserModel(String login, String password, RoleModel role) {
        this.login = login;
        this.password = password;
        this.role = role;
    }
    public UserModel() {}
}
