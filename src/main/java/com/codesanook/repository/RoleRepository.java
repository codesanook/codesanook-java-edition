package com.codesanook.repository;

import com.codesanook.model.Role;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

@Repository
public class RoleRepository {

    private static Log log = LogFactory.getLog(RoleRepository.class);
    @PersistenceContext
    EntityManager entityManager;

    public void addRole(Role role) {
        entityManager.persist(role);
    }

    public Role getRoleById(int roleId) {
        Role role = entityManager.find(Role.class, roleId);
        return role;
    }

    public void getCurrentFlushMode() {
        log.info(String.format("current flush mode %s", entityManager.getFlushMode()));
    }

    public List<Role> getAllRoles() {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Role> criteria = builder.createQuery(Role.class);
        criteria.select(criteria.from(Role.class));
        return entityManager.createQuery(criteria).getResultList();
    }
}
