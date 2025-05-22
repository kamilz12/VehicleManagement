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

    private boolean FETCH5 = true; // DEBUG FLAGA DOBRA DO TESTOWANIA

    private final String apiUrl = AppConstants.API_URL;
    private final int threadPoolSize = AppConstants.THREAD_POOL_SIZE;

    private final ExecutorService executor = Executors.newFixedThreadPool(threadPoolSize);
    private final Map<String, List<Integer>> yearsCache = new HashMap<>();

    // Metoda do włączania/wyłączania trybu FETCH5
    public void setFetch5Mode(boolean enabled) {
        this.FETCH5 = enabled;
        log.info("Tryb pobierania ograniczony do 5 elementów: {}", enabled);
    }

    // Metoda do sprawdzania aktualnego stanu flagi FETCH5
    public boolean isFetch5Mode() {
        return this.FETCH5;
    }

    public List<Integer> fetchYears() {
        if (yearsCache.containsKey("years")) {
            return yearsCache.get("years");
        }

        List<Integer> years = fetchFromApi(apiUrl + "/vehicle/menu/year?format=json", FuelEconomyDTO.class).map(dto -> dto.getMenuItem().stream().map(item -> Integer.parseInt(item.getValue())).collect(Collectors.toList())).orElseGet(() -> {
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

        // Licznik pobranych pojazdów i flaga zatrzymania
        final AtomicInteger vehicleCounter = new AtomicInteger(0);
        final AtomicBoolean shouldStop = new AtomicBoolean(false);

        List<CompletableFuture<Void>> futures = new ArrayList<>();

        // Iterujemy po latach i markach, tworząc zadania pobierania
        for (Integer year : years) {
            for (String make : makes) {
                // Jeśli flaga FETCH5 jest aktywna i już mamy 5 pojazdów, nie tworzymy więcej zadań
                if (FETCH5 && vehicleCounter.get() >= 5) {
                    log.info("Zatrzymano pobieranie po osiągnięciu 5 pojazdów");
                    shouldStop.set(true);
                    break;
                }

                // Tworzymy nowe zadanie
                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                    // Jeśli flaga zatrzymania jest aktywna, natychmiast kończymy
                    if (shouldStop.get()) {
                        return;
                    }

                    List<String> models = fetchModels(make, year.toString());
                    for (String model : models) {
                        // Ponownie sprawdzamy, czy mamy już 5 pojazdów
                        if (FETCH5 && vehicleCounter.get() >= 5) {
                            shouldStop.set(true);
                            return;
                        }

                        Map<Integer, String> engines = importEngineAndIDByModelAndMake(make, model, year);
                        for (Map.Entry<Integer, String> entry : engines.entrySet()) {
                            Integer engineId = entry.getKey();
                            String engineName = entry.getValue();

                            if (make != null && model != null && engineName != null && engineId != null &&
                                    !engineName.isEmpty() && !make.isEmpty() && !model.isEmpty() && year != null) {

                                vehicles.add(VehicleDTO.builder()
                                        .year(year)
                                        .make(make)
                                        .model(model)
                                        .engineName(engineName)
                                        .engineInternId(engineId)
                                        .build());

                                // Zwiększamy licznik i sprawdzamy, czy osiągnęliśmy limit
                                if (FETCH5 && vehicleCounter.incrementAndGet() >= 5) {
                                    log.info("Osiągnięto limit 5 pojazdów");
                                    shouldStop.set(true);
                                    return;
                                }
                            } else {
                                log.warn("One of the values is null: year={}, make={}, model={}, engineName={}, engineId={}",
                                        year, make, model, engineName, engineId);
                            }
                        }
                    }
                }, executor);

                futures.add(future);
            }

            // Jeśli flaga zatrzymania jest aktywna, przerywamy zewnętrzną pętlę
            if (shouldStop.get()) {
                break;
            }
        }

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[0]));

        try {
            if (FETCH5) {
                allFutures.get(10, TimeUnit.SECONDS);
            } else {
                allFutures.join();
            }
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            log.warn("Przerwano oczekiwanie na zakończenie wszystkich zadań: {}", e.getMessage());
        }

        // Jeśli mamy więcej niż 5 pojazdów a FETCH5=true, obcinamy listę
        if (FETCH5 && vehicles.size() > 5) {
            log.info("Ograniczanie końcowej listy pojazdów z {} do 5", vehicles.size());
            return vehicles.subList(0, 5);
        }

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
        // 1) Spróbuj pobrać z API
        List<String> apiMakes = fetchFromApi(
                apiUrl + "/ympg/shared/menu/make?format=json",
                FuelEconomyDTO.class
        ).map(dto -> dto.getMenuItem()
                .stream()
                .map(FuelEconomyMenuItem::getValue)
                .collect(Collectors.toList())
        ).orElse(Collections.emptyList());

        // 2) Jeżeli się udało – zwróć rezultat
        if (!apiMakes.isEmpty()) {
            log.info("Pobrano {} marek z API", apiMakes.size());
            return apiMakes;
        }

        // 3) W przeciwnym razie wczytaj plik zapasowy
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
                    log.info("Successfully imported {} models for make: {}, year: {}", models.size(), make, year); // Log successful import
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
                .map(dto -> {
                    Map<Integer, String> engines = dto.getMenuItem().stream()
                            .collect(Collectors.toMap(item -> Integer.parseInt(item.getValue()), FuelEconomyMenuItem::getText));
                    log.info("Successfully imported {} engines for make: {}, model: {}, year: {}", engines.size(), make, model, year); // Log successful import
                    return engines;
                })
                .orElseGet(() -> {
                    log.error("Error importing engines for make: {}, model: {}, year: {}", make, model, year);
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
                        T fullResponse = objectMapper.readValue(jsonResponse, responseType);

                        // Limitowanie do 5 elementów dla FuelEconomyDTO tylko gdy FETCH5=true
                        if (fullResponse instanceof FuelEconomyDTO) {
                            FuelEconomyDTO dto = (FuelEconomyDTO) fullResponse;
                            if (FETCH5 && dto.getMenuItem() != null && dto.getMenuItem().size() > 5) {
                                log.info("Ograniczanie liczby elementów z {} do 5 dla URL: {}",
                                        dto.getMenuItem().size(), url);
                                dto.setMenuItem(dto.getMenuItem().subList(0, 5));
                            }
                        }

                        return Optional.ofNullable(fullResponse);
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
