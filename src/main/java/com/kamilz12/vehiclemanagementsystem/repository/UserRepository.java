package com.kamilz12.vehiclemanagementsystem.repository;

import com.kamilz12.vehiclemanagementsystem.model.vehicle.User;

public interface UserRepository{
    User findByUserName(String theUserName);
    void save(User theUser);
    User findUserById(Long id);
}
