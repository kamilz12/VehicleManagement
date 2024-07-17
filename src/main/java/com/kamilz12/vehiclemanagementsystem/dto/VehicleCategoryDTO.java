package com.kamilz12.vehiclemanagementsystem.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class VehicleCategoryDTO {

    private Integer year;
    private String make;
    private List<String> models;
    private List<String> engines;

}
