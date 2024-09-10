package com.kamilz12.vehiclemanagementsystem.webclient.fueleconomy;

import com.kamilz12.vehiclemanagementsystem.configuration.AppConstants;
import com.kamilz12.vehiclemanagementsystem.dto.VehicleDTO;
import com.kamilz12.vehiclemanagementsystem.model.vehicle.Vehicle;
import com.kamilz12.vehiclemanagementsystem.webclient.fueleconomy.dto.FuelEconomyDTO;
import com.kamilz12.vehiclemanagementsystem.webclient.fueleconomy.dto.FuelEconomyMenuItem;
import com.kamilz12.vehiclemanagementsystem.webclient.fueleconomy.dto.FuelEconomyVehicle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Component
@Slf4j
public class VehicleClient {
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();


    private final String apiUrl = AppConstants.API_URL;
    private final int threadPoolSize = AppConstants.THREAD_POOL_SIZE;

    private final ExecutorService executor = Executors.newFixedThreadPool(threadPoolSize);
    private final Map<String, List<Integer>> yearsCache = new HashMap<>();

    public List<Integer> fetchYears() {
        if (yearsCache.containsKey("years")) {
            return yearsCache.get("years");
        }

        List<Integer> years = fetchFromApi(apiUrl + "/vehicle/menu/year", FuelEconomyDTO.class).map(dto -> dto.getMenuItem().stream().map(item -> Integer.parseInt(item.getValue())).collect(Collectors.toList())).orElseGet(() -> {
            log.error("Received null or empty response for years");
            return new ArrayList<>();
        });

        yearsCache.put("years", years);
        return years;
    }

    public List<VehicleDTO> fetchVehicles() {
        List<VehicleDTO> vehicles = Collections.synchronizedList(new ArrayList<>());
        List<String> makes = fetchMakes();
        List<Integer> years = fetchYears();

        List<CompletableFuture<Void>> futures = years.stream().flatMap(year -> makes.stream().map(make -> fetchAndAddVehiclesForYearAndMake(year, make, vehicles))).toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        return vehicles;
    }

    private CompletableFuture<Void> fetchAndAddVehiclesForYearAndMake(Integer year, String make, List<VehicleDTO> vehicles) {
        return CompletableFuture.runAsync(() -> {

            List<String> models = fetchModels(make, year.toString());
            models.forEach(model -> {
                Map<Integer, String> engines = importEngineAndIDByModelAndMake(make, model, year);
                engines.forEach((engineId, engineName) -> {
                    if (make != null && model != null && engineName != null && engineId != null && !engineName.isEmpty() && !make.isEmpty() && !model.isEmpty() && year != null) {
                        vehicles.add(VehicleDTO.builder().year(year).make(make).model(model).engineName(engineName).engineInternId(engineId).build());
                    } else {
                        log.error("One of the values is null: year={}, make={}, model={}, engineName={}, engineId={}", year, make, model, engineName, engineId);
                    }
                });
            });
        }, executor);
    }

    public Vehicle vehicleDTOtoVehicleDAO(VehicleDTO vehicleDTO) {
        Vehicle vehicleDAO = new Vehicle();
        vehicleDAO.setEngineName(vehicleDTO.getEngineName());
        vehicleDAO.setInternRestId(vehicleDTO.getEngineInternId());
        vehicleDAO.setYear(vehicleDTO.getYear());
        vehicleDAO.setMake(vehicleDTO.getMake());
        vehicleDAO.setModel(vehicleDTO.getModel());
        return vehicleDAO;
    }

    public List<String> fetchMakes() {
        return fetchFromApi(apiUrl + "/ympg/shared/menu/make", FuelEconomyDTO.class).map(dto -> dto.getMenuItem().stream().map(FuelEconomyMenuItem::getValue).collect(Collectors.toList())).orElseGet(() -> {
            log.error("Error importing makes");
            return new ArrayList<>();
        });
    }

    public List<String> fetchModels(String make, String year) {
        String url = String.format("%s/vehicle/menu/model?year=%s&make=%s", apiUrl, year, make);
        return fetchFromApi(url, FuelEconomyDTO.class).map(dto -> dto.getMenuItem().stream().map(FuelEconomyMenuItem::getValue).collect(Collectors.toList())).orElseGet(() -> {
            log.error("Error importing models for make: {}, year: {}", make, year);
            return new ArrayList<>();
        });
    }

    public Map<Integer, String> importEngineAndIDByModelAndMake(String make, String model, Integer year) {
        String url = String.format("%s/vehicle/menu/options?year=%d&make=%s&model=%s", apiUrl, year, make, model);
        return fetchFromApi(url, FuelEconomyDTO.class)
                .map(dto -> dto.getMenuItem().stream()
                        .collect(Collectors.toMap(item -> Integer.parseInt(item.getValue()), FuelEconomyMenuItem::getText))).orElseGet(() -> {
            log.error("Error importing engines for make, model, year: {}, {}, {}", make, model, year);
            return new HashMap<>();
        });
    }

    public VehicleDTO fetchYourVehicleConsumptionData(Integer id) {
        String url = String.format("%s/vehicle/%s", apiUrl, id);
        FuelEconomyVehicle fuelEconomyVehicle = restTemplate.getForObject(url, FuelEconomyVehicle.class);

        if (fuelEconomyVehicle != null) {

            VehicleDTO.VehicleDTOBuilder builder = VehicleDTO.builder();
            builder.make(fuelEconomyVehicle.getMake());
            builder.model(fuelEconomyVehicle.getModel());
            builder.year(Integer.valueOf(fuelEconomyVehicle.getYear()));
            builder.fuelType1(fuelEconomyVehicle.getFuelType1());
            builder.city08(fuelEconomyVehicle.getCity08());
            builder.highway08(fuelEconomyVehicle.getHighway08());
            builder.youSaveSpend(fuelEconomyVehicle.getYouSaveSpend());
            builder.engineName(fuelEconomyVehicle.getBaseModel());
            builder.engineInternId(Integer.valueOf(fuelEconomyVehicle.getId()));
            return builder.build();
        } else {
            log.error("Received null response from API: {}", url);
        }
        return null;
    }


    private <T> Optional<T> fetchFromApi(String url, Class<T> responseType, Object... uriVariables) {
        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class, uriVariables);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                String jsonResponse = responseEntity.getBody();

                if (jsonResponse != null && !jsonResponse.isEmpty()) {
                    return Optional.ofNullable(objectMapper.readValue(jsonResponse, responseType));
                } else {
                    log.error("Received null or empty response body from API: {}", url);
                }
            } else {
                log.error("Received non-success response status {} from API: {}", responseEntity.getStatusCode(), url);
            }
        } catch (RestClientException e) {
            log.error("Error calling external API: {}", url, e);
        } catch (Exception e) {
            log.error("Error processing response from external API: {}", url, e);
        }
        return Optional.empty();
    }
}
