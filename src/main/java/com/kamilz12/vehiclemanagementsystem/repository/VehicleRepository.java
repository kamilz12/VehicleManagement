package com.kamilz12.vehiclemanagementsystem.repository;

import com.kamilz12.vehiclemanagementsystem.model.vehicle.UserVehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<UserVehicle,Integer> {
}
