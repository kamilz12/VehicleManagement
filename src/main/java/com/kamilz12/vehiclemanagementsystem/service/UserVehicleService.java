package com.kamilz12.vehiclemanagementsystem.service;

import com.kamilz12.vehiclemanagementsystem.model.vehicle.UserVehicle;
import com.kamilz12.vehiclemanagementsystem.repository.UserVehicleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserVehicleService {
    private final UserVehicleRepository userVehicleRepository;

    public UserVehicleService(UserVehicleRepository userVehicleRepository) {
        this.userVehicleRepository = userVehicleRepository;

    }

    public void save(UserVehicle userVehicle) {
        //avoid duplicates with userid,vehicleid, vin and with samevin. for example
        //userid,vehicleid,vin
        //1,1,cdd22
        //1,1,cdd22 -> wrong
        //and situation when somebody want to add a vehicle with same vin but different vehicle values
        //1,2,cdd22e -> ok
        //1,3,cdd22e -> wrong
        //but it allows user has many vehicles and vehicles has many user
        //1,2,cdd22e -> ok
        //2,2,cdd22e -> ok
        List<UserVehicle> userVehiclesDatabase = userVehicleRepository.findUserVehicleByUserIdAndVehicleIdAndVin(
                userVehicle.getUser().getId(), userVehicle.getVehicle().getId(), userVehicle.getVin());
        List<UserVehicle> vehiclesWithSameVin = userVehicleRepository.findByVin(userVehicle.getVin());

        boolean isUniqueEntry = userVehiclesDatabase.isEmpty() && vehiclesWithSameVin.isEmpty();

        if (isUniqueEntry) {
            log.info("Saving user vehicle: {}", userVehicle);
            userVehicleRepository.save(userVehicle);
        } else {
            log.warn("Duplicate UserVehicle found: {}", userVehicle);
        }
    }
    public List<UserVehicle> findAll(){
        return userVehicleRepository.findAll();
    }
    public List<UserVehicle> findAllByUserId(Long id){
        return userVehicleRepository.findAllByUserId(id);
    }
}



