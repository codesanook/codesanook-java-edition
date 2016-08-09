package com.codesanook.model;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Column(columnDefinition = "TEXT", length = 5000,nullable = false)
    private String content;

    @Column(columnDefinition = "TEXT", length = 10000,nullable = false)
    private String htmlContent;


    @ManyToMany
    @JoinTable(name = "comment_uploaded_files",
            joinColumns = @JoinColumn(name = "comment_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "uploaded_file_id", referencedColumnName = "id"))
    private List<UploadedFile> uploadedFiles;


    @ManyToOne
    @JoinColumn(name = "post_id",nullable = false,
            foreignKey = @ForeignKey(name = "FK_posts_post_id"))
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false,
    foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    private User user;

    //foreignKey = @ForeignKey(name = "FK_posts_user_id")
    private boolean isDeleted;
    private DateTime utcCreateDate;
    private DateTime utcLastUpdate;
    private String userIp;



    public void delete() {
        isDeleted = true;
    }

    public void restore() {
        isDeleted = false;
    }

    public void validateRequriedProperties() {
        if (StringUtils.isEmpty(content))
            throw new IllegalStateException("content can not be null or empty");
        if (user == null)
            throw new IllegalStateException("user can not be null");
        if (StringUtils.isEmpty(userIp))
            throw new IllegalStateException("userIp can not be null or empty");
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Post getPost() {
        return post;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
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

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public void setPost(Post post) {
        this.post = post;
    }


    public List<UploadedFile> getUploadedFiles() {
        return uploadedFiles;
    }

    public void setUploadedFiles(List<UploadedFile> uploadedFiles) {
        this.uploadedFiles = uploadedFiles;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }
}
