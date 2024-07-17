package com.kamilz12.vehiclemanagementsystem.webclient.fueleconomy;

import com.kamilz12.vehiclemanagementsystem.webclient.fueleconomy.dto.FuelEconomyDTO;
import com.kamilz12.vehiclemanagementsystem.webclient.fueleconomy.dto.FuelEconomyMakeModelEngineDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
/*
My MPG Shared Data
This resource provides access to My MPG shared data via vehicle make and model:

Menu of shared make /ws/rest/ympg/shared/menu/make
Menu of shared models for the specified make; for example /ws/rest/ympg/shared/menu/model?make=Honda
List of vehicles with shared My MPG data; for example /ws/rest/ympg/shared/vehicles?make=Honda&model=Fit
 */


@Component
@Slf4j
public class VehicleClient {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String API_URL = "https://www.fueleconomy.gov/ws/rest";

    private final ExecutorService executor = Executors.newFixedThreadPool(10); // Customize the thread pool size based on your requirements
    private final Map<String, List<Integer>> yearsCache = new HashMap<>();
    public List<Integer> findAvailableYearsForMakeAndModel(String make, String model) {
        String cacheKey = make + "-" + model;
        // Check cache first
        if (yearsCache.containsKey(cacheKey)) {
            return yearsCache.get(cacheKey);
        }
        String apiUrl = API_URL + "/vehicle/menu/year?make=" + make + "&model=" + model;
        List<Integer> years = new ArrayList<>();
        FuelEconomyDTO response =callGetMethod(apiUrl,FuelEconomyDTO.class);
        if (response != null && response.getMenuItem() != null) {
            CompletableFuture[] futures = response.getMenuItem().stream()
                    .map(item -> CompletableFuture.supplyAsync(() -> {
                                int year = Integer.parseInt(item.getValue());
                                Map<Integer, String> engineOptions = importEngineAndIDByModelAndMake(make, model, String.valueOf(year));
                                return engineOptions != null && !engineOptions.isEmpty() ? year : null;
                            }, executor)
                            .exceptionally(ex -> {
                                log.error("Error processing year " + item.getValue(), ex);
                                return null;
                            }))
                    .toArray(CompletableFuture[]::new);

            CompletableFuture.allOf(futures).join(); // Wait for all futures to complete

            years = Arrays.stream(futures)
                    .map(future -> ((CompletableFuture<Integer>) future).join())
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }

        // Cache the result
        yearsCache.put(cacheKey, years);
        return years;
    }

    public List<String> importMakes() {
        FuelEconomyDTO fuelEconomyDTO = callGetMethod(API_URL + "/ympg/shared/menu/make", FuelEconomyDTO.class);
        List<String> vehicleMakes = new ArrayList<>();
        if (fuelEconomyDTO != null && fuelEconomyDTO.getMenuItem() != null) {
            for (FuelEconomyMakeModelEngineDTO item : fuelEconomyDTO.getMenuItem()) {
                vehicleMakes.add(item.getValue());
            }
        }
        return vehicleMakes;
    }

    public List<String> importModel(String make) {
        FuelEconomyDTO fuelEconomyDTO = callGetMethod(API_URL + "/ympg/shared/menu/model?make={make}", FuelEconomyDTO.class, make);
        List<String> models = new ArrayList<>();
        if (fuelEconomyDTO != null && fuelEconomyDTO.getMenuItem() != null) {
            for (FuelEconomyMakeModelEngineDTO item : fuelEconomyDTO.getMenuItem()) {
                models.add(item.getValue());
            }
        }
        return models;
    }

    public Map<Integer, String> importEngineAndIDByModelAndMake(String make, String vehicleModel, String year) {
        FuelEconomyDTO fuelEconomyDTO = callGetMethod(API_URL + "/vehicle/menu/options?make={make}&model={vehicleModel}&year={year}", FuelEconomyDTO.class, make, vehicleModel, year);
        Map<Integer, String> engineMap = new HashMap<>();
        if (fuelEconomyDTO != null && fuelEconomyDTO.getMenuItem() != null) {
            for (FuelEconomyMakeModelEngineDTO item : fuelEconomyDTO.getMenuItem()) {
                engineMap.put(Integer.parseInt(item.getValue()), item.getText());
            }
        }
        return engineMap;
    }

    // Modify the callGetMethod to include try-catch block for error handling
    private <T> T callGetMethod(String url, Class<T> responseType, Object... objects) {
        try {
            return restTemplate.getForObject(url, responseType, objects);
        } catch (Exception e) {
            log.error("Error calling GET method for URL: " + url, e);
            return null;
        }
    }


}
