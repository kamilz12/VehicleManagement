package com.kamilz12.vehiclemanagementsystem.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VehicleDTO {
    private Integer year;
    private String make;
    private String model;
    private Integer engineInternId;
    private String engineName;
    private String youSaveSpend;
    private String city08;
    private String highway08;
    private String fuelType1;
}
