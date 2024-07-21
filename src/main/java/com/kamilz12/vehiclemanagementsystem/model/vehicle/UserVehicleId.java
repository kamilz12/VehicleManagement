package com.kamilz12.vehiclemanagementsystem.model.vehicle;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
public class UserVehicleId implements Serializable {
    @Column(name = "users_id")
    private Long usersId;
    @Column(name = "vehicle_id")
    private Long vehicleId;

    public UserVehicleId(Long userId, Long vehicleId) {
        this.usersId = userId;
        this.vehicleId = vehicleId;
    }

    public UserVehicleId() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserVehicleId that)) return false;
        return Objects.equals(getUsersId(), that.getUsersId()) &&
                Objects.equals(getVehicleId(), that.getVehicleId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsersId(), getVehicleId());
    }
}
