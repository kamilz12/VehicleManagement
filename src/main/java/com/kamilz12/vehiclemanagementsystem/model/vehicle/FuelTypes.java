package com.kamilz12.vehiclemanagementsystem.model.vehicle;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "fuel_types")
public class FuelTypes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "fuel_type", nullable = false)
    private String fuelType;

    @OneToMany(mappedBy = "fuelType")
    private List<Vehicle> vehicles;

}
