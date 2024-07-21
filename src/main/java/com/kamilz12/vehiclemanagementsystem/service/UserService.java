package com.kamilz12.vehiclemanagementsystem.service;

import com.kamilz12.vehiclemanagementsystem.dto.UserDTO;
import com.kamilz12.vehiclemanagementsystem.model.vehicle.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User findUserByUsername(String userName);
    Long findLoggedUserIdByUsername();
    void save(UserDTO userDTO);
    User findUserById(Long id);
}
