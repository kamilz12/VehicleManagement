package com.kamilz12.vehiclemanagementsystem.webclient.fueleconomy.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FuelEconomyMenuItem {
    private String text;
    private String value;
}