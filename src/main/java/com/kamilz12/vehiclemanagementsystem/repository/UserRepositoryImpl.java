package com.kamilz12.vehiclemanagementsystem.repository;

import com.kamilz12.vehiclemanagementsystem.model.vehicle.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepository{
    private final EntityManager entityManager;

    public UserRepositoryImpl(EntityManager theEntityManager) {
        this.entityManager = theEntityManager;
    }

    @Override
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

    @Transactional
    public void save(User theUser) {
        entityManager.merge(theUser);
    }

    @Override
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
