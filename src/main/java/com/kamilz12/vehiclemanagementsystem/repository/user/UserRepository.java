package com.kamilz12.vehiclemanagementsystem.repository.user;

import com.kamilz12.vehiclemanagementsystem.model.vehicle.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByUserName(String theUserName);
    void save(User theUser);
    Optional<User> findUserById(Long id);
}
