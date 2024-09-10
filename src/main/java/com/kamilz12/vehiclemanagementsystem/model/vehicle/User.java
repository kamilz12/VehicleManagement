package com.kamilz12.vehiclemanagementsystem.model.vehicle;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull(message = "Value can't be null")
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @NotNull(message = "Value can't be null")
    @Column(name = "password", nullable = false)
    private String password;


    @NotNull(message = "Value can't be null")
    @Column(name = "enabled", nullable = false)
    private boolean enabled;


    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "users_has_authorities",
            joinColumns = @JoinColumn(name = "users_id"),
            inverseJoinColumns = @JoinColumn(name = "authorities_id"))
    private List<Role> roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserVehicle> userVehicles = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return enabled == user.enabled && Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(roles, user.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, enabled);
    }
}
