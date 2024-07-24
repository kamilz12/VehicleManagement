package com.kamilz12.vehiclemanagementsystem.repository.role;

import com.kamilz12.vehiclemanagementsystem.model.vehicle.Role;


public interface RoleRepository {
    Role findRoleByName(String theRoleName);
}
