package org.example.avtodiller.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "role")
public class RoleModel {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private String name;
}
