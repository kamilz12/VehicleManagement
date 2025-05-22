package com.kamilz12.vehiclemanagementsystem.webclient.fueleconomy;

import com.fasterxml.jackson.databind.JsonNode;
import com.kamilz12.vehiclemanagementsystem.configuration.AppConstants;
import com.kamilz12.vehiclemanagementsystem.dto.VehicleDTO;
import com.kamilz12.vehiclemanagementsystem.model.vehicle.Vehicle;
import com.kamilz12.vehiclemanagementsystem.webclient.fueleconomy.dto.FuelEconomyDTO;
import com.kamilz12.vehiclemanagementsystem.webclient.fueleconomy.dto.FuelEconomyMenuItem;
import com.kamilz12.vehiclemanagementsystem.webclient.fueleconomy.dto.FuelEconomyVehicle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.kamilz12.vehiclemanagementsystem.configuration.AppConstants.FALLBACK_MAKES_FILE;

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

        List<Integer> years = fetchFromApi(apiUrl + "/vehicle/menu/year?format=json", FuelEconomyDTO.class)
                .map(dto -> dto.getMenuItem().stream().map(item -> Integer.parseInt(item.getValue())).collect(Collectors.toList()))
                .orElseGet(() -> {
                    log.error("Received null or empty response for years");
                    return new ArrayList<>();
                });

        yearsCache.put("years", years);
        return years;
    }

    public List<VehicleDTO> fetchVehicles() {
        long startTime = System.currentTimeMillis();

        List<VehicleDTO> vehicles = Collections.synchronizedList(new ArrayList<>());
        List<String> makes = fetchMakes();
        List<Integer> years = fetchYears();

        // Możliwość ograniczenia dla testów
        // makes = makes.stream().filter(m -> List.of("Toyota", "Ford").contains(m)).collect(Collectors.toList());
        // years = years.stream().filter(y -> y >= 2020).collect(Collectors.toList());

        Map<String, List<String>> modelCache = new ConcurrentHashMap<>();
        Map<String, Map<Integer, String>> engineCache = new ConcurrentHashMap<>();

        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (Integer year : years) {
            for (String make : makes) {
                String makeYearKey = make + "_" + year;

                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                    List<String> models = modelCache.computeIfAbsent(makeYearKey, k -> fetchModels(make, year.toString()));
                    for (String model : models) {
                        String engineKey = make + "_" + model + "_" + year;
                        Map<Integer, String> engines = engineCache.computeIfAbsent(engineKey, k -> importEngineAndIDByModelAndMake(make, model, year));

                        for (Map.Entry<Integer, String> entry : engines.entrySet()) {
                            Integer engineId = entry.getKey();
                            String engineName = entry.getValue();

                            if (make != null && model != null && engineName != null && engineId != null &&
                                    !engineName.isEmpty() && !make.isEmpty() && !model.isEmpty()) {

                                vehicles.add(VehicleDTO.builder()
                                        .year(year)
                                        .make(make)
                                        .model(model)
                                        .engineName(engineName)
                                        .engineInternId(engineId)
                                        .build());
                            } else {
                                log.warn("Null/empty value during processing: year={}, make={}, model={}, engineName={}, engineId={}",
                                        year, make, model, engineName, engineId);
                            }
                        }
                    }
                }, executor);

                futures.add(future);
            }
        }

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        try {
            allFutures.join();
        } catch (Exception e) {
            log.warn("Interrupted while waiting for tasks to complete: {}", e.getMessage());
        }

        long endTime = System.currentTimeMillis();
        log.info("fetchVehicles() completed with {} vehicles in {} ms", vehicles.size(), (endTime - startTime));

        return vehicles;
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
        List<String> apiMakes = fetchFromApi(
                apiUrl + "/ympg/shared/menu/make?format=json",
                FuelEconomyDTO.class
        ).map(dto -> dto.getMenuItem()
                .stream()
                .map(FuelEconomyMenuItem::getValue)
                .collect(Collectors.toList())
        ).orElse(Collections.emptyList());

        if (!apiMakes.isEmpty()) {
            log.info("Pobrano {} marek z API", apiMakes.size());
            return apiMakes;
        }

        try (InputStream is = getClass().getResourceAsStream(FALLBACK_MAKES_FILE)) {
            if (is == null) {
                log.error("Nie znaleziono pliku fallback: {}", FALLBACK_MAKES_FILE);
                return new ArrayList<>();
            }

            JsonNode root = objectMapper.readTree(is);
            JsonNode items = root.get("menuItems");

            if (items == null || !items.isArray()) {
                log.error("Plik fallback ma niepoprawną strukturę");
                return new ArrayList<>();
            }

            List<String> fallbackMakes = new ArrayList<>();
            items.forEach(node -> fallbackMakes.add(node.get("value").asText()));

            log.warn("Użyto danych z pliku fallback ({} marek)", fallbackMakes.size());
            return fallbackMakes;
        } catch (IOException ex) {
            log.error("Błąd podczas wczytywania pliku fallback", ex);
            return new ArrayList<>();
        }
    }

    public List<String> fetchModels(String make, String year) {
        String url = String.format("%s/vehicle/menu/model?year=%s&make=%s&format=json", apiUrl, year, make);
        return fetchFromApi(url, FuelEconomyDTO.class)
                .map(dto -> {
                    List<String> models = dto.getMenuItem().stream()
                            .map(FuelEconomyMenuItem::getValue)
                            .collect(Collectors.toList());
                    log.info("Successfully imported {} models for make: {}, year: {}", models.size(), make, year);
                    return models;
                })
                .orElseGet(() -> {
                    log.warn("Importing models for make: {}, year: {}", make, year);
                    return new ArrayList<>();
                });
    }

    public Map<Integer, String> importEngineAndIDByModelAndMake(String make, String model, Integer year) {
        String url = String.format("%s/vehicle/menu/options?year=%d&make=%s&model=%s&format=json", apiUrl, year, make, model);
        return fetchFromApi(url, FuelEconomyDTO.class)
                .map(dto -> dto.getMenuItem().stream()
                        .collect(Collectors.toMap(item -> Integer.parseInt(item.getValue()), FuelEconomyMenuItem::getText)))
                .orElseGet(() -> {
                    log.error("Error importing engines for make: {}, model: {}, year: {}", make, model, year);
                    return new HashMap<>();
                });
    }

    public VehicleDTO fetchYourVehicleConsumptionData(Integer id) {
        String url = String.format("%s/vehicle/%s", apiUrl, id);
        FuelEconomyVehicle fuelEconomyVehicle = restTemplate.getForObject(url, FuelEconomyVehicle.class);

        if (fuelEconomyVehicle != null) {
            return VehicleDTO.builder()
                    .make(fuelEconomyVehicle.getMake())
                    .model(fuelEconomyVehicle.getModel())
                    .year(Integer.valueOf(fuelEconomyVehicle.getYear()))
                    .fuelType1(fuelEconomyVehicle.getFuelType1())
                    .city08(fuelEconomyVehicle.getCity08())
                    .highway08(fuelEconomyVehicle.getHighway08())
                    .youSaveSpend(fuelEconomyVehicle.getYouSaveSpend())
                    .engineName(fuelEconomyVehicle.getBaseModel())
                    .engineInternId(Integer.valueOf(fuelEconomyVehicle.getId()))
                    .build();
        } else {
            log.error("Received null response from API: {}", url);
        }
        return null;
    }

    private <T> Optional<T> fetchFromApi(String url, Class<T> responseType, Object... uriVariables) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", "application/json");
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, String.class, uriVariables);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                String jsonResponse = responseEntity.getBody();

                if (jsonResponse != null && !jsonResponse.isEmpty()) {
                    if (jsonResponse.trim().startsWith("<")) {
                        log.error("Received HTML instead of JSON from API: {}. First 100 chars: {}",
                                url, jsonResponse.substring(0, Math.min(100, jsonResponse.length())));
                        return Optional.empty();
                    }

                    try {
                        return Optional.ofNullable(objectMapper.readValue(jsonResponse, responseType));
                    } catch (Exception e) {
                        log.error("Error parsing JSON response from API: {}. First 100 chars: {}",
                                url, jsonResponse.substring(0, Math.min(100, jsonResponse.length())), e);
                        return Optional.empty();
                    }
                } else {
                    log.error("Received null or empty response body from API: {}", url);
                }
            } else {
                log.error("Received non-success response status {} from API: {}. Response body: {}",
                        responseEntity.getStatusCode(), url,
                        responseEntity.getBody() != null ? responseEntity.getBody().substring(0, Math.min(100, responseEntity.getBody().length())) : "null");
            }
        } catch (RestClientException e) {
            log.error("Error calling external API: {}", url, e);
        } catch (Exception e) {
            log.error("Error processing response from external API: {}", url, e);
        }
        return Optional.empty();
    }
}
