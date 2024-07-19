package com.kamilz12.vehiclemanagementsystem.service;

import com.kamilz12.vehiclemanagementsystem.model.vehicle.UserVehicle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserVehicleService {
    private final UserVehicleService userVehicleRepository;

    public UserVehicleService(@Lazy UserVehicleService userVehicleRepository) {
        this.userVehicleRepository = userVehicleRepository;
    }

    public void save(UserVehicle userVehicle){
        this.userVehicleRepository.save(userVehicle);
    }
}
