package com.kamilz12.vehiclemanagementsystem.repository.uservehicle;

import com.kamilz12.vehiclemanagementsystem.model.vehicle.UserVehicle;

import java.util.List;


public interface UserVehicleRepositoryCustom {

    // TODO: When you willing to use those methods uncomment otherwise delete
//    List<UserVehicle> findUserVehicleByUserIdAndVehicleIdAndVin(Long userId, Long vehicleId, String vin);

    void saveVehicle(UserVehicle userVehicle);

//    List<UserVehicle> findByVin(String vin);
//    List<UserVehicle> findAll();
    List<UserVehicle> findAllByUsersId(Long userId);

    UserVehicle findVehicleById(Long id);

    void deleteVehicleById(Long id);
}
