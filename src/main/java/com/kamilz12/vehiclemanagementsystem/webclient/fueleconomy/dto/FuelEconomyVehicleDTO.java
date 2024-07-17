package com.kamilz12.vehiclemanagementsystem.webclient.fueleconomy.dto;

import lombok.Getter;
import lombok.Setter;

/*Class used to import information from API to DTO*/
@Getter
@Setter
public class FuelEconomyVehicleDTO {
    private String atvtype;

    // Annual petroleum consumption in barrels for fuelType1
    private double barrels08;

    // Annual petroleum consumption in barrels for fuelType2
    private double barrelsA08;

    // Time to charge an electric vehicle in hours at 120 V
    private double charge120;

    // Time to charge an electric vehicle in hours at 240 V
    private double charge240;

    // City MPG for fuelType1
    private double city08;

    // Unrounded city MPG for fuelType1
    private double city08U;

    // City MPG for fuelType2
    private double cityA08;

    // Unrounded city MPG for fuelType2
    private double cityA08U;

    // City gasoline consumption (gallons/100 miles) in charge depleting mode
    private double cityCD;

    // City electricity consumption in kw-hrs/100 miles
    private double cityE;

    // City miles per Kilogram for Hydrogen
    private double cityMpk;

    // Unrounded city miles per Kilogram for Hydrogen
    private double cityUmpk;

    // EPA city utility factor (share of electricity) for PHEV
    private double cityUF;

    // Tailpipe CO2 in grams/mile for fuelType1
    private double co2;

    // Tailpipe CO2 in grams/mile for fuelType2
    private double co2A;

    // Tailpipe CO2 in grams/mile for fuelType2
    private double co2TailpipeAGpm;

    // Tailpipe CO2 in grams/mile for fuelType1
    private double co2TailpipeGpm;

    // Combined MPG for fuelType1
    private double comb08;

    // Unrounded combined MPG for fuelType1
    private double comb08U;

    // Combined MPG for fuelType2
    private double combA08;

    // Unrounded combined MPG for fuelType2
    private double combA08U;

    // Combined electricity consumption in kw-hrs/100 miles
    private double combE;

    // Combined miles per Kilogram for Hydrogen
    private double combMpk;

    // Unrounded combined miles per Kilogram for Hydrogen
    private double combUmpk;

    // Combined gasoline consumption (gallons/100 miles) in charge depleting mode
    private double combinedCD;

    // EPA combined utility factor (share of electricity) for PHEV
    private double combinedUF;

    // Engine cylinders
    private int cylinders;

    // Engine displacement in liters
    private double displ;

    // Drive axle type
    private String drive;

    // List of emissions
    private String emissionsList;

    // EPA model type index
    private int engId;

    // Engine descriptor
    private String eng_dscr;

    // Electric motor power in kw-hrs
    private double evMotor;

    // EPA Fuel Economy Score
    private int feScore;

    // Annual fuel cost for fuelType1
    private double fuelCost08;

    // Annual fuel cost for fuelType2
    private double fuelCostA08;

    // Fuel type with fuelType1 and fuelType2 (if applicable)
    private String fuelType;

    // Fuel type 1
    private String fuelType1;

    // Fuel type 2
    private String fuelType2;

    // EPA GHG score
    private int ghgScore;

    // EPA GHG score for dual fuel vehicle running on the alternative fuel
    private int ghgScoreA;

    // Indicates if the vehicle is subject to the gas guzzler tax
    private String guzzler;

    // Highway MPG for fuelType1
    private double highway08;

    // Unrounded highway MPG for fuelType1
    private double highway08U;

    // Highway MPG for fuelType2
    private double highwayA08;

    // Unrounded highway MPG for fuelType2
    private double highwayA08U;

    // Highway gasoline consumption (gallons/100miles) in charge depleting mode
    private double highwayCD;

    // Highway electricity consumption in kw-hrs/100 miles
    private double highwayE;

    // Highway miles per Kilogram for Hydrogen
    private double highwayMpk;

    // Unrounded highway miles per Kilogram for Hydrogen
    private double highwayUmpk;

    // EPA highway utility factor (share of electricity) for PHEV
    private double highwayUF;

    // Hatchback luggage volume (cubic feet)
    private double hlv;

    // Hatchback passenger volume (cubic feet)
    private double hpv;

    // Vehicle record id
    private int id;

    // 2 door luggage volume (cubic feet)
    private double lv2;

    // 4 door luggage volume (cubic feet)
    private double lv4;

    // Manufacturer (division)
    private String make;

    // 3-character manufacturer code
    private String mfrCode;

    // Model name (carline)
    private String model;

    // Indicates if the vehicle has My MPG data
    private String mpgData;

    // Indicates if this vehicle operates on a blend of gasoline and electricity in charge depleting mode
    private boolean phevBlended;

    // 2-door passenger volume (cubic feet)
    private double pv2;

    // 4-door passenger volume (cubic feet)
    private double pv4;

    // EPA range for fuelType2
    private double rangeA;

    // EPA city range for fuelType2
    private double rangeCityA;

    // EPA highway range for fuelType2
    private double rangeHwyA;

    // Transmission descriptor
    private String trans_dscr;

    // Transmission
    private String trany;

    // Unadjusted city MPG for fuelType1
    private double UCity;

    // Unadjusted city MPG for fuelType2
    private double UCityA;

    // Unadjusted highway MPG for fuelType1
    private double UHighway;

    // Unadjusted highway MPG for fuelType2
    private double UHighwayA;

    // EPA vehicle size class
    private String VClass;

    // Model year
    private int year;

    // You save/spend over 5 years compared to an average car ($). Savings are positive; a greater amount spent yields a negative number.
    private double youSaveSpend;

    // Indicates if the vehicle is supercharged
    private String sCharger;

    // Indicates if the vehicle is turbocharged
    private String tCharger;

    // Electric vehicle charger description
    private String c240Dscr;

    // Time to charge an electric vehicle in hours at 240 V using the alternate charger
    private double charge240b;

    // Electric vehicle alternate charger description
    private String c240bDscr;

    // Date the vehicle record was created
    private String createdOn;

    // Date the vehicle record was last modified
    private String modifiedOn;

    // Indicates if the vehicle has stop-start technology
    private String startStop;

    // EPA composite gasoline-electricity city MPGe for plug-in hybrid vehicles
    private double phevCity;

    // EPA composite gasoline-electricity highway MPGe for plug-in hybrid vehicles
    private double phevHwy;

    // EPA composite gasoline-electricity combined city-highway MPGe for plug-in hybrid vehicles
    private double phevComb;

    // Base model name
    private String basemodel;
}
