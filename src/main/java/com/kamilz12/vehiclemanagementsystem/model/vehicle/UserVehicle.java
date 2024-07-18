package com.kamilz12.vehiclemanagementsystem.model.vehicle;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "uservehicle")
public class UserVehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "make", nullable = false)
    private String make;
    @Column(name = "model")
    private String model;
    @Column(name = "year")
    private Integer year;
    @Column(name = "intern_rest_id")
    private Integer internRestId;
    @Column(name = "enginename")
    private String engineName;
    public UserVehicle() {

    }
}
