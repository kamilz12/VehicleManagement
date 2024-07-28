package com.kamilz12.vehiclemanagementsystem.model.vehicle;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
@Data
@Setter
@Getter
@Entity (name = "Vehicle")
@Table (name = "vehicle")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull(message = "Value can't be null")
    @NotBlank(message = "Make is required")
    @Column(name = "make", nullable = false)
    private String make;

    @NotNull(message = "Model is required")
    @NotBlank(message = "Model is required")
    @Column(name = "model", nullable = false)
    private String model;

    @NotNull(message = "Value can't be null")
    @Column(name = "year", nullable = false)
    private Integer year;

    @NotNull(message = "Value can't be null")
    @Column(name = "intern_rest_id", nullable = false)
    private Integer internRestId;

    @NotNull(message = "Engine name is required")
    @NotBlank(message = "Engine name is required")
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
