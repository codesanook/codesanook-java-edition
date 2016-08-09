package com.codesanook.repository;

import com.codesanook.model.*;
import com.codesanook.query.TagPostCount;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

@Repository
public class PostRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Post> getAllPosts(int firstItemIndex, int itemsPerPage, boolean isAsc,
                                  PostStatus postStatus) {

        String hql = "select p from Post p where p.postStatus.id = :postStatusId";
        if (isAsc) {
            hql += " order by p.id asc";
        } else {
            hql += " order by p.id desc";
        }
        Query query = entityManager.createQuery(hql, Post.class);
        query.setParameter("postStatusId", postStatus.getId());
        query.setFirstResult(firstItemIndex);
        query.setMaxResults(itemsPerPage);
        return query.getResultList();
    }


    public List<Post> getPostsByTag(Tag tag, int firstItemIndex, int itemsPerPage,
                                    boolean isAsc, PostStatus postStatus) {
        String sql = "SELECT p FROM Post p INNER JOIN p.tags t  " +
                "WHERE t.id = :tagId and p.postStatus.id = :postStatusId";
        if (isAsc) {
            sql += " ORDER BY p.id asc";
        } else {
            sql += " ORDER BY p.id desc";
        }

        Query query = entityManager.createQuery(sql, Post.class);
        query.setParameter("postStatusId", postStatus.getId());
        query.setParameter("tagId", tag.getId());
        query.setFirstResult(firstItemIndex);
        query.setMaxResults(itemsPerPage);

        List<Post> result = query.getResultList();
        return result;
    }


    public List<Post> getPostsByType(int postTypeId, int firstItemIndex, int itemsPerPage,
                                     boolean isAsc, PostStatus postStatus) {
        String sql = "SELECT p FROM Post p WHERE p.postType.id = :postTypeId and p.postStatus.id = :postStatusId";
        if (isAsc) {
            sql += " ORDER BY p.id asc";
        } else {
            sql += " ORDER BY p.id desc";
        }

        Query query = entityManager.createQuery(sql, Post.class);
        query.setParameter("postStatusId", postStatus.getId());
        query.setParameter("postTypeId", postTypeId);
        query.setFirstResult(firstItemIndex);
        query.setMaxResults(itemsPerPage);

        List<Post> result = query.getResultList();
        return result;
    }


    public List<Post> getPostsBySubtype(int postSubtypeId, int firstItemIndex, int itemsPerPage,
                                        boolean isAsc, PostStatus postStatus) {
        String sql = "SELECT p FROM Post p WHERE p.postSubtype.id = :postSubtypeId " +
                     "and p.postStatus.id = :postStatusId";
        if (isAsc) {
            sql += " ORDER BY p.id asc";
        } else {
            sql += " ORDER BY p.id desc";
        }

        Query query = entityManager.createQuery(sql, Post.class);
        query.setParameter("postStatusId", postStatus.getId());
        query.setParameter("postSubtypeId", postSubtypeId);
        query.setFirstResult(firstItemIndex);
        query.setMaxResults(itemsPerPage);

        List<Post> result = query.getResultList();
        return result;
    }


    public int countPost() {
        String sql = "select count (p.id) from Post p";
        Query query;
        query = entityManager.createQuery(sql);
        Long result = (Long) query.getSingleResult();
        return result.intValue();
    }

    public int countPost(Tag tag) {
        String sql = "select count (p.id) from Post p join p.tags t where t.id = :tagId";
        Query query;
        query = entityManager.createQuery(sql);
        query.setParameter("tagId", tag.getId());
        Long result = (Long) query.getSingleResult();
        return result.intValue();
    }


    public int countPost(PostType postType) {
        String sql = "select count (p.id) from Post p where p.postType.id = :postTypeId";
        Query query;
        query = entityManager.createQuery(sql);
        query.setParameter("postTypeId", postType.getId());
        Long result = (Long) query.getSingleResult();
        return result.intValue();
    }


    public int countPost(PostSubtype postSubtype) {
        String sql = "select count (p.id) from Post p " +
                "where p.postSubtype.id = :postSubtypeId";
        Query query;
        query = entityManager.createQuery(sql);
        query.setParameter("postSubtypeId", postSubtype.getId());
        Long result = (Long) query.getSingleResult();
        return result.intValue();
    }

    public List<TagPostCount> getTagPostCounts() {
//        String sql = "select new com.codesanook.query.TagPostCount( " +
//                "t.id, t.name, count(elements(t.posts)) ) " +
//                "from Tag t group by  t.id";

        String sql = "SELECT  tag_id as id, " +
                "t.name as name,  " +
                "count(post_id) as postCount " +
                "FROM post_tags pt " +
                "INNER JOIN tags t ON t.id = pt.tag_id " +
                "GROUP BY tag_id";
        Query query = entityManager.createNativeQuery(sql, TagPostCount.mappingName);
        return query.getResultList();
    }


    public void addTag(Tag tag) {
        entityManager.persist(tag);
    }


    public void addPost(Post post) {
        entityManager.persist(post);
    }

    public Post getPostById(int postId) {
        return entityManager.find(Post.class, postId);
    }

    public List<Tag> getAllTags() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteria = builder.createQuery(Tag.class);
        criteria.select(criteria.from(Tag.class));
        return entityManager.createQuery(criteria).getResultList();
    }

    public Tag getTagById(int tagId) {

        Query query = entityManager.createQuery("SELECT t FROM Tag t WHERE t.id = :id", Tag.class);
        query.setParameter("id", tagId);
        List<Tag> result = query.getResultList();
        if (!result.isEmpty()) {
            return result.get(0);
        }
        return null;
    }

    public void addComment(Comment comment) {
        entityManager.persist(comment);
    }

    public Comment getCommentById(int commentId) {
        return entityManager.find(Comment.class, commentId);
    }

    public List<Tag> searchTagByName(String tagName) {
        Query query = entityManager.createQuery(
                "SELECT t FROM Tag t WHERE t.name LIKE :tagName", Tag.class);
        query.setParameter("tagName", tagName + "%");
        List<Tag> result = query.getResultList();
        return result;

    }

    public Tag getTagByName(String tagName) {
        Query query = entityManager.createQuery("SELECT t FROM Tag t WHERE t.name = :name", Tag.class);
        query.setParameter("name", tagName);
        List<Tag> result = query.getResultList();
        if (!result.isEmpty()) {
            return result.get(0);
        }
        return null;
    }

    public PostStatus getPostStatusById(int postStatusId) {
        PostStatus postStatus = entityManager.getReference(PostStatus.class, postStatusId);
        return postStatus;
    }

    public PostType getPostTypeById(int postTypeId) {
        return entityManager.getReference(PostType.class, postTypeId);
    }

    public PostSubtype getPostSubtypeById(int postSubtypeId) {
        return entityManager.getReference(PostSubtype.class, postSubtypeId);
    }


    public PostType getPostTypeByName(String postTypeName) {
        Query query = entityManager.createQuery("SELECT t FROM PostType t WHERE t.name = :name", PostType.class);
        query.setParameter("name", postTypeName);
        List<PostType> result = query.getResultList();
        if (!result.isEmpty()) {
            return result.get(0);
        }
        return null;
    }

    public void addMultipartPost(MultipartPost multipartPost) {
        entityManager.persist(multipartPost);
    }


    public List<MultipartPost> searchMultipartPostsByTile(String title) {
//from User u where str(u.id) like :userId
        Query query = entityManager.createQuery("SELECT p FROM MultipartPost p WHERE p.title like :title",
                MultipartPost.class);
        query.setParameter("title", title + "%");
        List<MultipartPost> result = query.getResultList();
        return result;
    }

    public MultipartPost getMultipartPostById(int multipartPostId) {
        return entityManager.find(MultipartPost.class, multipartPostId);
    }

    public List<Post> searchPostByTitle(String title) {
        Query query = entityManager.createQuery("SELECT p FROM Post p WHERE p.title like :title",
                Post.class);
        query.setParameter("title", title + "%");
        List<Post> result = query.getResultList();
        return result;
    }

    public MultipartPostItem getMultipartPostItemByPostId(int postId) {
        return entityManager.find(MultipartPostItem.class, postId);
    }

    public void removeMultipartPostItem(MultipartPostItem multipartPostItem) {
        entityManager.remove(multipartPostItem);
    }


    public MultipartPost getMultipartPostByPostId(int postId) {
        Query query = entityManager.createQuery("SELECT i.multipartPost FROM MultipartPostItem i " +
                "WHERE i.postId = :postId", MultipartPost.class);

        query.setParameter("postId", postId);
        List<MultipartPost> result = query.getResultList();

        if (!result.isEmpty()) {
            return result.get(0);
        }
        return null;

    }


    public PostSubtype getPostSubtypeByName(String postSubtypeName) {
        Query query = entityManager.createQuery("SELECT t FROM PostSubtype t WHERE t.name = :name", PostSubtype.class);
        query.setParameter("name", postSubtypeName);
        List<PostSubtype> result = query.getResultList();
        if (!result.isEmpty()) {
            return result.get(0);
        }
        return null;
    }


}
