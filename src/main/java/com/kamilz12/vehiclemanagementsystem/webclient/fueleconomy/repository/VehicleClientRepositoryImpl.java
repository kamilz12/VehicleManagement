package com.kamilz12.vehiclemanagementsystem.webclient.fueleconomy.repository;

import com.kamilz12.vehiclemanagementsystem.dto.VehicleDTO;
import com.kamilz12.vehiclemanagementsystem.webclient.fueleconomy.VehicleClient;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class VehicleClientRepositoryImpl implements VehicleClientRepository {

    private final VehicleClient vehicleClient;

    public VehicleClientRepositoryImpl(VehicleClient vehicleClient) {
        this.vehicleClient = vehicleClient;
    }

    @SneakyThrows
    @Override
    public List<String> getMakes() {
        return vehicleClient.fetchMakes();
    }

    @SneakyThrows
    @Override
    public List<String> getModelByMake(String make, String year) {
        return vehicleClient.fetchModels(make, year);
    }

    @SneakyThrows
    @Override
    public Map<Integer, String> getEngineByMakeAndModel(String make, String model, Integer year) {
        return vehicleClient.importEngineAndIDByModelAndMake(make, model, year);
    }

    @SneakyThrows
    @Override
    public List<VehicleDTO> fetchAllDataFromDatabase() {
        return vehicleClient.fetchVehicles();
    }
}
