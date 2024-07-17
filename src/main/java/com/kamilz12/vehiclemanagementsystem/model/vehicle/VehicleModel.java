package com.kamilz12.vehiclemanagementsystem.model.vehicle;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@Entity
@Table(name = "models")
public class VehicleModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "model", nullable = false)
    private String model;

    @ManyToOne
    @JoinColumn(name = "make_id", nullable = false)
    private Make make;

    @OneToMany(mappedBy = "model")
    private List<Vehicle> vehicles;


}