package com.kamilz12.vehiclemanagementsystem.model.vehicle;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.security.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "vehicles")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "make_id", nullable = false)
    private Make make;

    @ManyToOne
    @JoinColumn(name = "model_id", nullable = false)
    private VehicleModel model;

    @ManyToOne
    @JoinColumn(name = "fuel_type_id", nullable = false)
    private FuelTypes fuelType;

    private String atvType;
    private float barrels08;
    private float barrelsA08;
    private int battery;
    private String c240Dscr;
    private String c240bDscr;
    private float charge120;
    private float charge240;
    private float charge240b;
    private int city08;
    private float city08U;
    private int cityA08;
    private float cityA08U;
    private float cityCD;
    private float cityE;
    private int cityMpk;
    private float cityUF;
    private float cityUmpk;
    private int co2;
    private int co2A;
    private float co2TailpipeAGpm;
    private float co2TailpipeGpm;
    private int comb08;
    private float comb08U;
    private int combA08;
    private float combA08U;
    private float combE;
    private int combMpk;
    private float combUmpk;
    private float combinedCD;
    private float combinedUF;
    private Timestamp createdOn;
    private char cylDeact;
    private String cylDeactYesNo;
    private int cylinders;
    private float displ;
    private String drive;
    private int engId;
    private String eng_dscr;
    private String evMotor;
    private int feScore;
    private int fuelCost08;
    private int fuelCostA08;
    private int ghgScore;
    private int ghgScoreA;
    private String guzzler;
    private int highway08;
    private float highway08U;
    private int highwayA08;
    private float highwayA08U;
    private float highwayCD;
    private float highwayE;
    private int highwayMpk;
    private float highwayUF;
    private float highwayUmpk;
    private int hlv;
    private int hpv;
    private int lv2;
    private int lv4;
    private String mfrCode;
    private Timestamp modifiedOn;
    private char mpgData;
    private String mpgRevised;
    private String phevBlended;
    private int phevCity;
    private int phevComb;
    private int phevHwy;
    private int pv2;
    private int pv4;
    private int vehicle_range;
    private String rangeA;
    private float rangeCity;
    private float rangeCityA;
    private float rangeHwy;
    private float rangeHwyA;
    private char startStop;
    private String trans_dscr;
    private String trany;
    private float UCity;
    private float UCityA;
    private float UHighway;
    private float UHighwayA;
    private String VClass;
    private int year;
    private int youSaveSpend;
    private String baseModel;
    private String sCharger;
    private String tCharger;
}
