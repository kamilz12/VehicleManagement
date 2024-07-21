package com.kamilz12.vehiclemanagementsystem.model.vehicle;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Setter
@Getter
@Entity (name = "UserVehicle")
@Table(name = "users_has_vehicle")
public class UserVehicle {

    @EmbeddedId
    private UserVehicleId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("usersId") // This maps the userId in UserVehicleId
    @JoinColumn(name = "users_id")
    private User users;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("vehicleId") // This maps the vehicleId in UserVehicleId
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;
    @Column(name ="vin")
    String vin;
    @Column(name = "mileage")
    Integer mileage;

    public UserVehicle() {
    }

    public UserVehicle(User users, Vehicle vehicle) {
        this.users = users;
        this.vehicle = vehicle;
        this.id = new UserVehicleId(users.getId(), vehicle.getId());
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserVehicle that = (UserVehicle) o;
        return Objects.equals(users, that.users) && Objects.equals(vehicle, that.vehicle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(users, vehicle);
    }
}
