package com.kamilz12.vehiclemanagementsystem.service.vehicle;

import com.kamilz12.vehiclemanagementsystem.model.vehicle.UserVehicle;
import com.kamilz12.vehiclemanagementsystem.model.vehicle.Vehicle;
import com.kamilz12.vehiclemanagementsystem.repository.uservehicle.UserVehicleRepositoryCustom;
import com.kamilz12.vehiclemanagementsystem.repository.vehicle.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleService {
    private final VehicleRepository vehicleRepository;
    private final UserVehicleRepositoryCustom userVehicleRepositoryCustom;

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository, UserVehicleRepositoryCustom userVehicleRepositoryCustom) {
        this.vehicleRepository = vehicleRepository;
        this.userVehicleRepositoryCustom = userVehicleRepositoryCustom;
    }

    // I wrongly deleted some of your unused methods, check before implement

    // TODO: Some logic, right now your service class just call repository
    // TODO: When some of present method doesn't return expected value
    // TODO: inform client, send appropriate Http.Status
    // TODO: ^^ Exception handling
    public void saveVehicle(UserVehicle userVehicle) {
        userVehicleRepositoryCustom.saveVehicle(userVehicle);
    }

    public List<UserVehicle> findAllVehicleByUserId(Long userId){
        return userVehicleRepositoryCustom.findAllByUsersId(userId);
    }

    public UserVehicle findVehicleById(Long id) {
        return userVehicleRepositoryCustom.findVehicleById(id);
    }

    public void deleteVehicleById(Long id) {
        userVehicleRepositoryCustom.deleteVehicleById(id);
    }
    public Vehicle findByInternRestId(Integer internRestId){
        return vehicleRepository.findByInternRestId(internRestId);
    }

    public List <Vehicle> findAll(){
        return vehicleRepository.findAll();
    }
}
