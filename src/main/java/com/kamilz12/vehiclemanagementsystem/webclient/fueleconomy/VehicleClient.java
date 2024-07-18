package com.kamilz12.vehiclemanagementsystem.webclient.fueleconomy;

import com.kamilz12.vehiclemanagementsystem.webclient.fueleconomy.dto.FuelEconomyDTO;
import com.kamilz12.vehiclemanagementsystem.webclient.fueleconomy.dto.FuelEconomyMakeModelEngineDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

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
        try {
            FuelEconomyDTO response = callGetMethod(apiUrl, FuelEconomyDTO.class);
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
        } catch (Exception e) {
            log.error("Error finding available years for make and model: " + make + " " + model, e);
        }
        return years;
    }

    public List<String> importMakes() {
        List<String> vehicleMakes = new ArrayList<>();
        try {
            FuelEconomyDTO fuelEconomyDTO = callGetMethod(API_URL + "/ympg/shared/menu/make", FuelEconomyDTO.class);
            if (fuelEconomyDTO != null && fuelEconomyDTO.getMenuItem() != null) {
                for (FuelEconomyMakeModelEngineDTO item : fuelEconomyDTO.getMenuItem()) {
                    vehicleMakes.add(item.getValue());
                }
            }
        } catch (Exception e) {
            log.error("Error importing makes", e);
        }
        return vehicleMakes;
    }

    public List<String> importModel(String make) {
        List<String> models = new ArrayList<>();
        try {
            String url = API_URL + "/ympg/shared/menu/model?make=" + make;
            FuelEconomyDTO fuelEconomyDTO = callGetMethod(url, FuelEconomyDTO.class);
            if (fuelEconomyDTO != null && fuelEconomyDTO.getMenuItem() != null) {
                for (FuelEconomyMakeModelEngineDTO item : fuelEconomyDTO.getMenuItem()) {
                    models.add(item.getValue());
                }
            }
        } catch (Exception e) {
            log.error("Error importing models for make: " + make, e);
        }
        return models;
    }

    public Map<Integer, String> importEngineAndIDByModelAndMake(String make, String vehicleModel, String year) {
        Map<Integer, String> engineMap = new HashMap<>();
        try {
            String url = API_URL + "/vehicle/menu/options?make=" + make + "&model=" + vehicleModel + "&year=" + year;
            FuelEconomyDTO fuelEconomyDTO = callGetMethod(url, FuelEconomyDTO.class);
            if (fuelEconomyDTO != null && fuelEconomyDTO.getMenuItem() != null) {
                for (FuelEconomyMakeModelEngineDTO item : fuelEconomyDTO.getMenuItem()) {
                    engineMap.put(Integer.parseInt(item.getValue()), item.getText());
                }
            }
        } catch (NumberFormatException e) {
            log.error("Error parsing integer value", e);
        } catch (Exception e) {
            log.error("Error importing engine and ID by model and make", e);
        }
        return engineMap;
    }

    // Modify the callGetMethod to include try-catch block for error handling
    private <T> T callGetMethod(String url, Class<T> responseType, Object... objects) {
        try {
            return restTemplate.getForObject(url, responseType, objects);
        } catch (RestClientException e) {
            log.error("Error calling GET method for URL: " + url, e);
            // Consider throwing a custom exception or returning a default object
            throw new CustomServiceException("Failed to fetch data from API", e);
        } catch (Exception e) {
            log.error("Unexpected error calling GET method for URL: " + url, e);
            throw new CustomServiceException("Unexpected error occurred", e);
        }
    }
}
