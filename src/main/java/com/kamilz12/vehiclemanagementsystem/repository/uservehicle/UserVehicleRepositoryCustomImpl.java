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
            if (userVehicle.getId() == null || findById(userVehicle.getId()) == null) {
                entityManager.persist(userVehicle);
            } else {
                entityManager.merge(userVehicle);
            }
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

    @Override
    public UserVehicle findById(Long id) {
        try {
            TypedQuery<UserVehicle> query = entityManager.createQuery("from UserVehicle uv where uv.id = :id", UserVehicle.class);
            query.setParameter("id", id);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    @Transactional
    @Override
    public void deleteById(Long id) {
        entityManager.remove(findById(id));
    }

}
