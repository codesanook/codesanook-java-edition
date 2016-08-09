package com.codesanook.repository;

import com.codesanook.model.Post;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class MarkDownRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Post getPost(int postId) {
        Post post = entityManager.getReference(Post.class, postId);
        return post;
    }
}
