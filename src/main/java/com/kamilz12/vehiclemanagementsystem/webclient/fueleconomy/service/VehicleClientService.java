package com.kamilz12.vehiclemanagementsystem.webclient.fueleconomy.service;

import com.kamilz12.vehiclemanagementsystem.configuration.AppConstants;
import com.kamilz12.vehiclemanagementsystem.dto.VehicleDTO;
import com.kamilz12.vehiclemanagementsystem.model.vehicle.Vehicle;
import com.kamilz12.vehiclemanagementsystem.repository.vehicle.VehicleRepository;
import com.kamilz12.vehiclemanagementsystem.service.vehicle.VehicleService;
import com.kamilz12.vehiclemanagementsystem.webclient.fueleconomy.repository.VehicleClientRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class VehicleClientService {
    VehicleService vehicleService;
    VehicleClientRepository vehicleClientRepository;

    public VehicleClientService(VehicleService vehicleService, VehicleClientRepository vehicleClientRepository) {
        this.vehicleService = vehicleService;
        this.vehicleClientRepository = vehicleClientRepository;
    }

    @Async
    @Transactional
    //method used to combine all data from api and save them to database
    public void fetchAndSaveALlVehiclesDataFromAPI() {
        try {
            List<VehicleDTO> allVehicleDTOList = vehicleClientRepository.fetchAllDataFromDatabase().stream().limit(5).toList();
            log.info("Fetched total of {} vehicles from API", allVehicleDTOList.size());

            List<VehicleDTO> vehicleDTOList = allVehicleDTOList.stream()
                    .toList();

            List<Vehicle> vehiclesFromDatabase = vehicleService.findAll();

            for (VehicleDTO vehicleDTO : vehicleDTOList) {
                try {
                    if (vehiclesFromDatabase.stream().noneMatch(vehicle -> vehicle.getInternRestId().equals(vehicleDTO.getEngineInternId()))) {
                        log.info("Trying to save vehicle: {} {} {}", vehicleDTO.getMake(), vehicleDTO.getModel(), vehicleDTO.getYear());
                        Vehicle vehicle = new Vehicle();
                        vehicle.setMake(vehicleDTO.getMake());
                        vehicle.setModel(vehicleDTO.getModel());
                        vehicle.setYear(vehicleDTO.getYear());
                        vehicle.setEngineName(vehicleDTO.getEngineName());
                        vehicle.setInternRestId(vehicleDTO.getEngineInternId());

                        vehicleService.save(vehicle);
                    } else {
                        log.info("Vehicle with id: {} already exists in the database", vehicleDTO.getEngineInternId());
                    }
                } catch (Exception e) {
                    log.error("Error saving vehicle with id: {}, error: {}", vehicleDTO.getEngineInternId(), e.getMessage(), e);
                }
            }
            log.info("Finished saving test batch of 5 vehicles");
        } catch (Exception e) {
            log.error("Error in fetchAndSaveALlVehiclesDataFromAPI: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Async
    @Transactional
    public void fetchAndSaveUpToFiveVehicles() {
        try {
            List<VehicleDTO> allVehicleDTOList = vehicleClientRepository.fetchAllDataFromDatabase();
            log.info("Fetched total of {} vehicles from API", allVehicleDTOList.size());

            List<Vehicle> vehiclesFromDatabase = vehicleService.findAll();
            int importedCount = 0;
            int maxImports = 5;

            // Iterujemy przez pojazdy z API
            for (VehicleDTO vehicleDTO : allVehicleDTOList) {
                try {
                    // Sprawdzamy czy pojazd już istnieje w bazie danych
                    boolean vehicleExists = vehiclesFromDatabase.stream()
                            .anyMatch(vehicle -> vehicle.getInternRestId().equals(vehicleDTO.getEngineInternId()));

                    if (!vehicleExists) {
                        log.info("Importing new vehicle: {} {} {}", vehicleDTO.getMake(), vehicleDTO.getModel(), vehicleDTO.getYear());
                        Vehicle vehicle = new Vehicle();
                        vehicle.setMake(vehicleDTO.getMake());
                        vehicle.setModel(vehicleDTO.getModel());
                        vehicle.setYear(vehicleDTO.getYear());
                        vehicle.setEngineName(vehicleDTO.getEngineName());
                        vehicle.setInternRestId(vehicleDTO.getEngineInternId());

                        vehicleService.save(vehicle);
                        importedCount++;

                        // Przerywamy pętlę po zaimportowaniu 5 pojazdów
                        if (importedCount >= maxImports) {
                            log.info("Reached the maximum limit of {} imported vehicles", maxImports);
                            break;
                        }
                    }
                } catch (Exception e) {
                    log.error("Error importing vehicle with id: {}, error: {}", vehicleDTO.getEngineInternId(), e.getMessage(), e);
                }
            }

            log.info("Successfully imported {} new vehicles", importedCount);
        } catch (Exception e) {
            log.error("Error occurred while importing vehicles: {}", e.getMessage(), e);
        }
    }

    public VehicleDTO fetchFuelConsumptionData(Integer id) {
        VehicleDTO vehicleDTO = vehicleClientRepository.getInfoAboutFuelConsumption(id);

        Vehicle vehicle = vehicleService.findByInternRestId(id);
        if (vehicle != null) {
            return VehicleDTO.builder()
                    .year(vehicleDTO.getYear())
                    .make(vehicleDTO.getMake())
                    .model(vehicleDTO.getModel())
                    .engineInternId(vehicleDTO.getEngineInternId())
                    .engineName(vehicle.getEngineName())
                    .youSaveSpend(vehicleDTO.getYouSaveSpend())
                    .city08(vehicleDTO.getCity08())
                    .highway08(vehicleDTO.getHighway08())
                    .fuelType1(vehicleDTO.getFuelType1())
                    .build();
        }

        return vehicleDTO;
    }
}
