package com.kamilz12.vehiclemanagementsystem.dto;

import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class FuelConsumptionDTO {
    private String vehicleInternId;
    private String avgMpg;
    private String cityPercent;
    private String highwayPercent;
    private String maxMpg;
    private String minMpg;
}
