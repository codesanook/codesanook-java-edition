package com.codesanook.model;

import org.joda.time.DateTime;

import javax.persistence.*;

@Entity
@Table(name = "multipart_post_items")
public class MultipartPostItem implements Comparable<MultipartPostItem> {

    @Id
    @Column(name = "post_id")
    private int postId;

    @MapsId
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id", nullable = false)

    private Post post;

    @Column(name = "order_index", nullable = false)
    private int orderIndex;

    @Column(nullable = false)
    private DateTime utcCreateDate;
    private DateTime utcLastUpdate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_multipart_post_items_user_id"))
    private User user;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "multipart_post_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_multipart_post_items_multipart_post_id"))
    private MultipartPost multipartPost;

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public int getPostId() {
        return postId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    public DateTime getUtcCreateDate() {
        return utcCreateDate;
    }

    public void setUtcCreateDate(DateTime utcCreateDate) {
        this.utcCreateDate = utcCreateDate;
    }

    public DateTime getUtcLastUpdate() {
        return utcLastUpdate;
    }

    public void setUtcLastUpdate(DateTime utcLastUpdate) {
        this.utcLastUpdate = utcLastUpdate;
    }

    @Override
    public int compareTo(MultipartPostItem other) {
        //0 1
        //2 2
        //3 2
        return this.orderIndex - other.getOrderIndex();
    }

    public MultipartPost getMultipartPost() {
        return multipartPost;
    }

    public void setMultipartPost(MultipartPost multipartPost) {
        this.multipartPost = multipartPost;
    }
}
