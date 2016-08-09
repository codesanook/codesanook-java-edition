package com.codesanook.repository;

import com.codesanook.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public void addUser(User user) {
        entityManager.persist(user);
    }

    public User getUserById(int userId) {
        User user = entityManager.getReference(User.class, userId);
        return user;
    }

    public User getUserByEmail(String email) {
        Query query = entityManager.createQuery("FROM User u WHERE u.email = :email", User.class);
        query.setParameter("email", email);
        List<User> users = query.getResultList();
        if (!users.isEmpty()) {
            return users.get(0);
        }
        return null;
    }

    public User getUserByApiKey(String apiKey) {
        Query query = entityManager.createQuery("FROM User u WHERE u.apiKey = :apiKey");
        query.setParameter("apiKey", apiKey);
        return (User) query.getSingleResult();
    }

    public List<User> getUsers(int firstItemIndex, int itemsPerPage) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteria = builder.createQuery(User.class);
        Root<User> pRoot = criteria.from(User.class);
        criteria.orderBy(builder.asc(pRoot.get("id")));

        return entityManager.createQuery(criteria)
                .setFirstResult(firstItemIndex) // offset
                .setMaxResults(itemsPerPage) // limit
                .getResultList();
    }

    public User getByFacebookId(long facebookId) {
        Query query = entityManager
                .createQuery("FROM User u WHERE u.facebookAppScopeUserId = :facebookId",
                        User.class);
        query.setParameter("facebookId", facebookId);
        List<User> users = query.getResultList();
        if (!users.isEmpty()) {
            return users.get(0);
        }
        return null;
    }
}

