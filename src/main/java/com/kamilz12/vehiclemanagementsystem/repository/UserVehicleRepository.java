package com.kamilz12.vehiclemanagementsystem.repository;

import com.kamilz12.vehiclemanagementsystem.model.vehicle.UserVehicle;

import java.util.List;


public interface UserVehicleRepository{
    List<UserVehicle> findUserVehicleByUserIdAndVehicleIdAndVin(Long userId, Long vehicleId, String vin);

    void save(UserVehicle userVehicle);

    List<UserVehicle> findByVin(String vin);
    List<UserVehicle> findAll();
    List<UserVehicle> findAllByUserId(Long userId);
}
