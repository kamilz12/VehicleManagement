package com.kamilz12.vehiclemanagementsystem.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FuelConsumptionDTO {
    private String vehicleInternId;
    private String avgMpg;
    private String cityPercent;
    private String highwayPercent;
    private String maxMpg;
    private String minMpg;
}
