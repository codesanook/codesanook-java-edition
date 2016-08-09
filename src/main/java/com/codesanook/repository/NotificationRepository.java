package com.codesanook.repository;

import com.codesanook.model.Post;
import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class NotificationRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Post> getLatestPosts(int count, int postTypeId) {
        String hql = "select p from Post p " +
                "where p.postType.id = :postTypeId " +
                "order by p.utcCreateDate desc";
        Query query = entityManager.createQuery(hql, Post.class);
        query.setParameter("postTypeId", postTypeId);
        query.setMaxResults(count);

        return query.getResultList();
    }

    public int getNewPostCount(DateTime lastRead, int postTypeId) {

        String hql = "select count(p.id) from Post p " +
                "where p.utcCreateDate > :lastRead " +
                "and p.postType.id = :postTypeId ";

        Query query = entityManager.createQuery(hql,Long.class);
        query.setParameter("lastRead", lastRead);
        query.setParameter("postTypeId", postTypeId);
        Long count = (Long) query.getSingleResult();
        return count.intValue();
    }
}
