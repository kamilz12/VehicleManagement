package com.kamilz12.vehiclemanagementsystem.repository;

import com.kamilz12.vehiclemanagementsystem.model.vehicle.UserVehicle;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserVehicleRepositoryImpl implements UserVehicleRepository{
    private final EntityManager entityManager;

    public UserVehicleRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<UserVehicle> findUserVehicleByUserIdAndVehicleIdAndVin(Long userId, Long vehicleId, String vin) {
        TypedQuery<UserVehicle> query = entityManager.createQuery(
                "from UserVehicle uv where uv.user.id = :usersId and uv.vehicle.id = :vehicleId and uv.vin = :vin", UserVehicle.class);
        query.setParameter("usersId", userId);
        query.setParameter("vehicleId", vehicleId);
        query.setParameter("vin", vin);
        return query.getResultList();
    }
    @Transactional
    @Override
    public void save(UserVehicle userVehicle) {
        try {
            entityManager.persist(userVehicle);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<UserVehicle> findByVin(String vin) {
        TypedQuery<UserVehicle> query = entityManager.createQuery(
                "from UserVehicle uv where uv.vin = :vin", UserVehicle.class);
        query.setParameter("vin", vin);
        return query.getResultList();
    }

    @Override
    public List<UserVehicle> findAll() {
        TypedQuery<UserVehicle> query = entityManager.createQuery("from UserVehicle", UserVehicle.class);
        return query.getResultList();
    }
    @Override
    public List<UserVehicle> findAllByUserId(Long userId) {
        TypedQuery<UserVehicle> query = entityManager.createQuery("from UserVehicle uv where uv.user.id =: userId", UserVehicle.class);
        query.setParameter("userId",userId);
        return query.getResultList();
    }

}
