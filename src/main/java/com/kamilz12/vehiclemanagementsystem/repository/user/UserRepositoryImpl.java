package com.kamilz12.vehiclemanagementsystem.repository.user;

import com.kamilz12.vehiclemanagementsystem.model.vehicle.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository
public class UserRepositoryImpl implements UserRepository{
    private final EntityManager entityManager;

    public UserRepositoryImpl(EntityManager theEntityManager) {
        this.entityManager = theEntityManager;
    }

    @Override
    @Transactional(readOnly = true)
    public User findByUserName(String theUserName) {

        TypedQuery<User> theQuery = entityManager.createQuery("from User where username=:uName and enabled=true", User.class);
        theQuery.setParameter("uName", theUserName);

        User theUser;
        try {
            theUser = theQuery.getSingleResult();
        } catch (Exception e) {
            theUser = null;
        }

        return theUser;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void save(User theUser) {
        entityManager.merge(theUser);
    }

    @Override
    @Transactional(readOnly = true)
    public User findUserById(Long id) {
        TypedQuery <User> query = entityManager.createQuery("from User where id=:uId and enabled=true", User.class);
        query.setParameter("uId", id);
        User user;
        try {
            user = query.getSingleResult();
        } catch (Exception e) {
            user = null;
        }
        return user;
    }
}
