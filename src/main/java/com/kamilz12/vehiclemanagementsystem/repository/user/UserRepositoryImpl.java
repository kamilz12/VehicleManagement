package com.kamilz12.vehiclemanagementsystem.repository.user;

import com.kamilz12.vehiclemanagementsystem.model.vehicle.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final EntityManager entityManager;

    public UserRepositoryImpl(EntityManager theEntityManager) {
        this.entityManager = theEntityManager;
    }

    @Override
    public Optional<User> findByUserName(String theUserName) {

        TypedQuery<User> theQuery = entityManager.createQuery("from User where username=:uName and enabled=true", User.class);
        theQuery.setParameter("uName", theUserName);

        return theQuery.getResultList().stream().findFirst();
    }

    @Transactional
    public void save(User theUser) {
        entityManager.merge(theUser);
    }

    @Override
    public Optional<User> findUserById(Long id) {
        TypedQuery <User> query = entityManager.createQuery("from User where id=:uId and enabled=true", User.class);
        query.setParameter("uId", id);

        return query.getResultList().stream().findFirst();
    }
}
