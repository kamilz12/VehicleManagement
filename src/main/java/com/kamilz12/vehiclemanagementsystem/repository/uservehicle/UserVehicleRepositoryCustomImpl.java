package com.kamilz12.vehiclemanagementsystem.repository.uservehicle;

import com.kamilz12.vehiclemanagementsystem.model.vehicle.UserVehicle;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserVehicleRepositoryCustomImpl implements UserVehicleRepositoryCustom {
    private final EntityManager entityManager;
    public UserVehicleRepositoryCustomImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

//    @Override
//    public List<UserVehicle> findUserVehicleByUserIdAndVehicleIdAndVin(Long userId, Long vehicleId, String vin) {
//        TypedQuery<UserVehicle> query = entityManager.createQuery(
//                "from UserVehicle uv where uv.user.id = :usersId and uv.vehicle.id = :vehicleId and uv.vin = :vin", UserVehicle.class);
//        query.setParameter("usersId", userId);
//        query.setParameter("vehicleId", vehicleId);
//        query.setParameter("vin", vin);
//        return query.getResultList();
//    }

    @Transactional
    @Override
    public void saveVehicle(UserVehicle userVehicle) {
        try {
            if (userVehicle.getId() == null || findVehicleById(userVehicle.getId()) == null) {
                entityManager.persist(userVehicle);
            } else {
                entityManager.merge(userVehicle);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

//    @Override
//    public List<UserVehicle> findByVin(String vin) {
//        TypedQuery<UserVehicle> query = entityManager.createQuery(
//                "from UserVehicle uv where uv.vin = :vin", UserVehicle.class);
//        query.setParameter("vin", vin);
//        return query.getResultList();
//    }
//
//    @Override
//    public List<UserVehicle> findAll() {
//        TypedQuery<UserVehicle> query = entityManager.createQuery("from UserVehicle", UserVehicle.class);
//        return query.getResultList();
//    }

    @Override
    public List<UserVehicle> findAllByUsersId(Long userId) {
        TypedQuery<UserVehicle> query = entityManager.createQuery("from UserVehicle uv where uv.user.id =: userId", UserVehicle.class);
        query.setParameter("userId",userId);
        return query.getResultList();
    }

    @Override
    public UserVehicle findVehicleById(Long id) {
        try {
            TypedQuery<UserVehicle> query = entityManager.createQuery("from UserVehicle uv where uv.id = :id", UserVehicle.class);
            query.setParameter("id", id);
            return query.getSingleResult();
        } catch (NoResultException e) {
            // TODO: Better exception handling, watch video form Amigo
            // you should return appropriate HTTP status
            // here is concept from Amigoscode
            // https://www.youtube.com/watch?v=PzK4ZXa2Tbc
            return null;
        }
    }
    @Transactional
    @Override
    public void deleteVehicleById(Long id) {
         // TODO: Validation, what happens
         // when vehicleId is not present?
        entityManager.remove(id);
    }

}
