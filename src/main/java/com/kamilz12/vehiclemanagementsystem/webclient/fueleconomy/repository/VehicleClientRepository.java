package com.kamilz12.vehiclemanagementsystem.webclient.fueleconomy.repository;

import com.kamilz12.vehiclemanagementsystem.dto.VehicleDTO;
import java.util.List;
import java.util.Map;

public interface VehicleClientRepository {
    List<String> getMakes();
    List<String> getModelByMake(String make, String year);
    Map<Integer, String> getEngineByMakeAndModel(String make, String model, Integer year);
    List<VehicleDTO> fetchAllDataFromDatabase();
}