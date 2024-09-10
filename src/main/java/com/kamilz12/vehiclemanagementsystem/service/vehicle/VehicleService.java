package com.kamilz12.vehiclemanagementsystem.service.vehicle;

import com.kamilz12.vehiclemanagementsystem.model.vehicle.Vehicle;
import com.kamilz12.vehiclemanagementsystem.repository.vehicle.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class VehicleService {
    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }
    public List <Vehicle> findAll(){
        return vehicleRepository.findAll();
    }

    public Set<String> findMakes(){
        return vehicleRepository.findAllDistinctMakesASC();
    }

    public Set <String> findModelsByMake(String make){
        return vehicleRepository.findAllDistinctModelsByMake(make);
    }

    public Set <Integer> findYears(String make, String model){
        return vehicleRepository.findAllDistinctYearsByModelAndYear(make, model);
    }

    public Set<String> findEngines(String make, String model, Integer year){
        return vehicleRepository.findAllDistinctEnginesByMakeModelYear(make,model,year);
    }

    public Integer findInternRestId(String make, String model, Integer year, String engine){
        return vehicleRepository.findDistinctInternRestIdByMakeModelYearEngineName(make,model,year,engine);
    }

    public Vehicle findByInternRestId(Integer internRestId){
        return vehicleRepository.findByInternRestId(internRestId);
    }


    public void save(Vehicle vehicle){
        this.vehicleRepository.save(vehicle);
    }

}