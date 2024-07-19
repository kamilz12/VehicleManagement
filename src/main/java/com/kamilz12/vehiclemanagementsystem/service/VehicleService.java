package com.kamilz12.vehiclemanagementsystem.service;

import com.kamilz12.vehiclemanagementsystem.model.vehicle.Vehicle;
import com.kamilz12.vehiclemanagementsystem.repository.VehicleRepository;
import com.kamilz12.vehiclemanagementsystem.webclient.fueleconomy.repository.VehicleClientRepository;
import com.kamilz12.vehiclemanagementsystem.webclient.fueleconomy.repository.VehicleClientRepositoryImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleService {
    private VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }
    public List <Vehicle> findAll(){
        return vehicleRepository.findAll();
    }

    public void save(Vehicle vehicle){
        this.vehicleRepository.save(vehicle);
    }

}
