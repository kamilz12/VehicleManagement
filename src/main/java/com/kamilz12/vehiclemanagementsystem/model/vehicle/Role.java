package com.kamilz12.vehiclemanagementsystem.model.vehicle;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "authorities")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotNull(message = "Value can't be null")
    private String authority;

    @NotNull(message = "Value can't be null")
    @ManyToMany(mappedBy = "roles")
    private List<User> users;

}