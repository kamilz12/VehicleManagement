package com.kamilz12.vehiclemanagementsystem.repository;

import com.kamilz12.vehiclemanagementsystem.model.vehicle.Role;


public interface RoleRepository {
    Role findRoleByName(String theRoleName);
}
