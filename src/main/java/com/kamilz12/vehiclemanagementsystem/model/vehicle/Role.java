package com.kamilz12.vehiclemanagementsystem.model.vehicle;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Entity
@Table(name = "authorities")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotNull(message = "Value can't be null")
    private String authority;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;

    public Role(String authority){
        this.authority = authority;
    }

    public Role() {

    }
}