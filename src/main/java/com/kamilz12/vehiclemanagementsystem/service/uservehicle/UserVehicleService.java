package com.kamilz12.vehiclemanagementsystem.service.uservehicle;

import com.kamilz12.vehiclemanagementsystem.model.vehicle.UserVehicle;
import com.kamilz12.vehiclemanagementsystem.repository.uservehicle.UserVehicleRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserVehicleService {
    private final UserVehicleRepositoryCustom userVehicleRepositoryCustom;

    public UserVehicleService(UserVehicleRepositoryCustom userVehicleRepositoryCustom) {
        this.userVehicleRepositoryCustom = userVehicleRepositoryCustom;
    }

    public void save(UserVehicle userVehicle) {
        userVehicleRepositoryCustom.save(userVehicle);
    }

    public List<UserVehicle> findByVin(UserVehicle userVehicle) {
        return userVehicleRepositoryCustom.findByVin(userVehicle.getVin());
    }

    public List<UserVehicle> findByUserIdVehicleIdVin(UserVehicle userVehicle) {
        return userVehicleRepositoryCustom.findUserVehicleByUserIdAndVehicleIdAndVin(
                userVehicle.getUser().getId(), userVehicle.getVehicle().getId(), userVehicle.getVin());
    }

    public List<UserVehicle> findAll() {
        return userVehicleRepositoryCustom.findAll();
    }

    public List<UserVehicle> findAllByUserId(Long userId) {
        return userVehicleRepositoryCustom.findAllByUserId(userId);
    }

    public UserVehicle findById(Long id) {
        return userVehicleRepositoryCustom.findById(id);
    }

    public void deleteById(Long id) {
        userVehicleRepositoryCustom.deleteById(id);
    }

}



