package com.kamilz12.vehiclemanagementsystem.service;

import com.kamilz12.vehiclemanagementsystem.model.vehicle.UserVehicle;
import com.kamilz12.vehiclemanagementsystem.repository.UserVehicleRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class UserVehicleService {
    private final UserVehicleRepository userVehicleRepository;

    public UserVehicleService(UserVehicleRepository userVehicleRepository) {
        this.userVehicleRepository = userVehicleRepository;
    }

    public void save(UserVehicle userVehicle){
        userVehicleRepository.save(userVehicle);
    }


}
