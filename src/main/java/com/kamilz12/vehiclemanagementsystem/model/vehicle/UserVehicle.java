package com.kamilz12.vehiclemanagementsystem.model.vehicle;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import java.util.Objects;

@Setter
@Getter
@Entity (name = "UserVehicle")
@Table(name = "users_has_vehicle")
@Builder
public class UserVehicle {

    @Column(name = "id")
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")

    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @Column(name ="vin", nullable = false)
    @NotNull(message = "Value can't be null")
    @NotBlank(message = "Value is not present, enter value")
    private String vin;

    @Column(name = "mileage", nullable = false)
    @Min(value = 0, message = "Mileage must be greater than or equal to 0")
    @NotNull(message = "Value can't be null")
    private int mileage;

    @NotNull(message = "Value can't be null")
    @Column(name = "owned", nullable = false)
    private boolean owned;

    public UserVehicle() {
    }
    public UserVehicle(User user, Vehicle vehicle) {
        this.user = user;
        this.vehicle = vehicle;
    }

    public UserVehicle(Long id, User user, Vehicle vehicle, String vin, int mileage, boolean owned) {
        this.id = id;
        this.user = user;
        this.vehicle = vehicle;
        this.vin = vin;
        this.mileage = mileage;
        this.owned = owned;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserVehicle that = (UserVehicle) o;
        return Objects.equals(user, that.user) && Objects.equals(vehicle, that.vehicle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, vehicle);
    }
}
