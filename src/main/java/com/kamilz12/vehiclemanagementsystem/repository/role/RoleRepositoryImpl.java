package com.kamilz12.vehiclemanagementsystem.repository.role;

import com.kamilz12.vehiclemanagementsystem.model.vehicle.Role;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Repository;

@Repository
public class RoleRepositoryImpl implements RoleRepository{
    private final EntityManager entityManager;

    public RoleRepositoryImpl(EntityManager theEntityManager) {
        this.entityManager = theEntityManager;
    }

    @Override
    @Transactional(readOnly = true)
    public Role findRoleByName(String theRoleName) {

        // retrieve/read from database using name
        TypedQuery<Role> theQuery = entityManager.createQuery("from Role where authority=:roleName", Role.class);
        theQuery.setParameter("roleName", theRoleName);

        Role theRole;

        try {
            theRole = theQuery.getSingleResult();
        } catch (Exception e) {
            theRole = null;
        }

        return theRole;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Override
    public void addRole(String name) {
        Role role = new Role(name);
        entityManager.persist(role);
    }
}
