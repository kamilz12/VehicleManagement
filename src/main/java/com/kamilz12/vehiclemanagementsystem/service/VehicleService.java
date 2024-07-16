package com.kamilz12.vehiclemanagementsystem.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
@Slf4j
@Service
public class VehicleService {

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String API_URL = "https://www.fueleconomy.gov/ws/rest/ympg/shared/vehicles";


    public String getVehiclesByMakeAndModel(String make, String model) {
        String url = String.format("%s?make=%s&model=%s", API_URL, make, model);
        return restTemplate.getForObject(url, String.class);
    }

}