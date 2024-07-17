package com.kamilz12.vehiclemanagementsystem.webclient.fueleconomy.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FuelEconomyDTO {
    //private List<FuelEconomyVehicleDTO> vehicle;
    private List<FuelEconomyMakeModelEngineDTO> menuItem;

}
