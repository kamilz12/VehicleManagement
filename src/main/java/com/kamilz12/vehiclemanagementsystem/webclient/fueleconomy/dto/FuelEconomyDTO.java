package com.kamilz12.vehiclemanagementsystem.webclient.fueleconomy.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FuelEconomyDTO {
    @JsonProperty("menuItem")
    @JsonDeserialize(using = MenuItemDeserializer.class)
    private List<FuelEconomyMenuItem> menuItem;

}
