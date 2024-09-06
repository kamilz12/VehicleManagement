package com.kamilz12.vehiclemanagementsystem.webclient.fueleconomy.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class FuelEconomyVehicle {
    String make;
    String model;
    String year;
    String youSaveSpend;
    String baseModel;
    String city08;
    String highway08;
    String fuelType1;
    String id;
}
