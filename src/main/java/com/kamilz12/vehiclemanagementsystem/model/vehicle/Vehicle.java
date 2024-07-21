package com.kamilz12.vehiclemanagementsystem.model.vehicle;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Setter
@Getter
@Entity (name = "Vehicle")
@Table (name = "vehicle")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)

    private Long id;
    @Column(name = "make", nullable = false)
    private String make;
    @Column(name = "model", nullable = false)
    private String model;
    @Column(name = "year", nullable = false)
    private Integer year;
    @Column(name = "intern_rest_id", nullable = false)
    private Integer internRestId;
    @Column(name = "enginename", nullable = false)
    private String engineName;
    public Vehicle() {

    }


    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL)
    public List<UserVehicle> userVehicles = new ArrayList<>();

    public void addUser(User user){
        UserVehicle userVehicle = new UserVehicle(user, this);
        userVehicles.add(userVehicle);
        user.getUserVehicles().add(userVehicle);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return Objects.equals(make, vehicle.make) && Objects.equals(model, vehicle.model) && Objects.equals(year, vehicle.year) && Objects.equals(internRestId, vehicle.internRestId) && Objects.equals(engineName, vehicle.engineName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(make, model, year, internRestId, engineName);
    }
}
