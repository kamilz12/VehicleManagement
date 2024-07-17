package com.kamilz12.vehiclemanagementsystem.model.vehicle;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "makes")
public class Make {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "make", nullable = false)
    private String make;

    @OneToMany(mappedBy = "make")
    private List<VehicleModel> models;

    public Make(String make) {
        this.make = make;
    }

    public Make() {

    }
}
