package com.kamilz12.vehiclemanagementsystem.service;

import com.kamilz12.vehiclemanagementsystem.repository.VehicleRepository;
import com.kamilz12.vehiclemanagementsystem.webclient.fueleconomy.VehicleClient;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class VehicleService {

    private final VehicleClient vehicleClient;
    private final VehicleRepository vehicleRepository;
    public VehicleService(VehicleClient vehicleClient, VehicleRepository vehicleRepository) {
        this.vehicleClient = vehicleClient;
        this.vehicleRepository = vehicleRepository;
    }
    @SneakyThrows
    public List<String> getMakes() {
        return vehicleClient.importMakes();
    }
    @SneakyThrows

    public List<String> getModelByMake(String model) {
        return vehicleClient.importModel(model);
    }
    @SneakyThrows
    public Map<Integer,String> getEngineByMakeAndModel(String make, String model,String year){
        return vehicleClient.importEngineAndIDByModelAndMake(make,model,year);
    }


    public List<Integer> findAvailableYearsForMakeAndModel(String make, String model) {
        return vehicleClient.findAvailableYearsForMakeAndModel(make,model);
    }
}
