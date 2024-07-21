package com.kamilz12.vehiclemanagementsystem.repository;

import com.kamilz12.vehiclemanagementsystem.model.vehicle.UserVehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository    ;
@Repository
public interface UserVehicleRepository extends JpaRepository<UserVehicle, Long> {


}
