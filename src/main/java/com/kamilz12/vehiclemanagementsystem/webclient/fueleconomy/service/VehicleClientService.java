package com.kamilz12.vehiclemanagementsystem.webclient.fueleconomy.service;

import com.kamilz12.vehiclemanagementsystem.dto.VehicleDTO;
import com.kamilz12.vehiclemanagementsystem.model.vehicle.Vehicle;
import com.kamilz12.vehiclemanagementsystem.repository.vehicle.VehicleRepository;
import com.kamilz12.vehiclemanagementsystem.webclient.fueleconomy.repository.VehicleClientRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class VehicleClientService {
    VehicleRepository vehicleRepository;
     VehicleClientRepository vehicleClientRepository;

    public VehicleClientService(VehicleRepository vehicleRepository, VehicleClientRepository vehicleClientRepository) {
        this.vehicleRepository = vehicleRepository;
        this.vehicleClientRepository = vehicleClientRepository;
    }

    //enable to fetch data from api
    //@PostConstruct
        public void fetchAndSaveALlVehiclesDataFromAPI(){
            List<VehicleDTO> vehicleDTOList = vehicleClientRepository.fetchAllDataFromDatabase();
            List <Vehicle> vehiclesFromDatabase = vehicleRepository.findAll();

            for (VehicleDTO vehicleDTO : vehicleDTOList) {
                if(vehiclesFromDatabase.stream().noneMatch(vehicle -> vehicle.getInternRestId().equals(vehicleDTO.getEngineInternId()))) {
                    log.info("Vehicle with id: {} does not exist in the database, saved!", vehicleDTO.getEngineInternId());
                    Vehicle vehicle = new Vehicle();
                    vehicle.setMake(vehicleDTO.getMake());
                    vehicle.setModel(vehicleDTO.getModel());
                    vehicle.setYear(vehicleDTO.getYear());
                    vehicle.setEngineName(vehicleDTO.getEngine_name());
                    vehicle.setInternRestId(vehicleDTO.getEngineInternId());
                    vehicleRepository.save(vehicle);}
            else {
                    log.info("Vehicle with id: {} already exists in the database", vehicleDTO.getEngineInternId());
                }
            }
        }

        public VehicleDTO fetchFuelConsumptionData (Integer id){
            return vehicleClientRepository.getInfoAboutFuelConsumption(id);
        }
}
