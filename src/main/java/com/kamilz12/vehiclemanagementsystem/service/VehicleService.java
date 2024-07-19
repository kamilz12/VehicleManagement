package com.kamilz12.vehiclemanagementsystem.service;

import com.kamilz12.vehiclemanagementsystem.model.vehicle.Vehicle;
import com.kamilz12.vehiclemanagementsystem.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleService {
    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }
    public List <Vehicle> findAll(){
        return vehicleRepository.findAll();
    }

    public List<String> findMakes(){
        return vehicleRepository.findAllDistinctMakesASC();
    }

    public List <String> findModelsByMake(String make){
        return vehicleRepository.findAllDistinctModelsByMake(make);
    }

    public List <Integer> findYears(String make, String model){
        return vehicleRepository.findAllDistinctYearsByModelAndYear(make, model);
    }

    public List <String> findEngines(String make, String model, Integer year){
        return vehicleRepository.findAllDistinctEnginesByMakeModelYear(make,model,year);
    }

    public Integer findInternRestID(String make, String model, Integer year, String engine){
        return vehicleRepository.findDistinctInternRestIdByMakeModelYearEngineName(make,model,year,engine);
    }


    public void save(Vehicle vehicle){
        this.vehicleRepository.save(vehicle);
    }

}
