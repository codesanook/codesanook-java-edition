package com.codesanook.model;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@Entity
@Table(name = "posts")
@Access(value = AccessType.FIELD)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String alias;

    @Column(length = 1000, nullable = true)
    private String shortIntroduction;


    @Column(columnDefinition = "TEXT", length = 20000, nullable = false)
    private String content;

    @Column(columnDefinition = "TEXT", length = 40000, nullable = true)
    private String htmlContent;

    @ManyToOne
    @JoinColumn(name = "featured_image_id", nullable = true)
    private UploadedFile featuredImage;

    private int pageViewCount;


    // Will be mapped as DATETIME (on MySQL)
    //@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    // Will be mapped as DATE (on MySQL), i.e. only date without timestamp
    //@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    private DateTime utcReleaseDate;

    //@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime utcCreateDate;
    //@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime utcLastUpdate;

    @ManyToMany
    @JoinTable(name = "post_tags",
            joinColumns = @JoinColumn(name = "post_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id"))
    private List<Tag> tags;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "post")
    private List<Comment> comments;


    @ManyToMany
    @JoinTable(name = "post_uploaded_files",
            joinColumns = @JoinColumn(name = "post_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "uploaded_file_id", referencedColumnName = "id"))
    private List<UploadedFile> uploadedFiles;


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    @ManyToOne
    @JoinColumn(name = "post_status_id", nullable = false)
    private PostStatus postStatus;

    @ManyToOne
    @JoinColumn(name = "post_type_id", nullable = false)
    private PostType postType;


    @ManyToOne
    @JoinColumn(name = "post_subtype_id", nullable = true)
    private PostSubtype postSubtype;


    private boolean isMultipartPost;
    private boolean isDeleted;

    public Post() {
        tags = new ArrayList<>();
        comments = new ArrayList<>();
        uploadedFiles = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void increasePageViewCount() {
        pageViewCount++;
    }


    public void validateRequiredProperty() {
        if (StringUtils.isEmpty(title))
            throw new IllegalStateException("title is empty");

        if (StringUtils.isEmpty(title))
            throw new IllegalStateException("alias is empty");

        if (StringUtils.isEmpty(content))
            throw new IllegalStateException("content is empty");

        if (user == null)
            throw new IllegalStateException("user is null");

        if (tags.size() == 0)
            throw new IllegalStateException("at least 1 tag");

//        DateTime utcNow = new DateTime(DateTimeZone.UTC);
//        if (utcReleaseDate.before(utcNow.toDate()))
//            throw new IllegalStateException(" is empty");
    }


    public void addTagIfNotExist(Tag newTagToAdd) {
        HashMap<String, String> existingTagNames = new HashMap<>();
        for (Tag existingTag : tags) {
            existingTagNames.put(existingTag.getName(), existingTag.getName());
        }

        String foundTag = existingTagNames.get(newTagToAdd.getName());
        if (foundTag != null) return;

        this.tags.add(newTagToAdd);
        newTagToAdd.getPosts().add(this);
    }

    public void removeTagsIfMissing(List<String> newTagNames) {
        if (newTagNames == null) return;
        if (newTagNames.size() == 0) return;
        if (tags.size() == 0) return;

        HashMap<String, String> newTagNameDictionary = new HashMap<>();
        for (String tagName : newTagNames) {
            newTagNameDictionary.put(tagName, tagName);
        }
        //search tag to remove

        Iterator<Tag> tagIterator = tags.iterator();
        while (tagIterator.hasNext()) {

            Tag existingTag = tagIterator.next(); // must be called before you can call i.remove()
            String foundKey = newTagNameDictionary.get(existingTag.getName());
            //not found
            if (foundKey == null) {
                tagIterator.remove();
                existingTag.getPosts().remove(this);
            }
        }


//        for(int index =0;index < tags.size(); index ++){
//            Tag existingTag = tags.get(index);
//            String foundKey = newTagNameDictionary.get(existingTag.getName());
//
//            //not found
//            if (foundKey == null) {
//                tags.remove(existingTag);
//                existingTag.getPosts().remove(this);
//            }
//        }
    }

    public void addComment(Comment comment) {
        comment.setPost(this);
        this.comments.add(comment);
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
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

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        if (!StringUtils.isEmpty(alias)) {
            alias = alias.replaceAll(" ", "-");
        }
        this.alias = alias;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUtcReleaseDate(DateTime utcReleaseDate) {

        this.utcReleaseDate = utcReleaseDate;
    }

    public int getPageViewCount() {
        return pageViewCount;
    }

    public void setPageViewCount(int pageViewCount) {
        this.pageViewCount = pageViewCount;
    }

    public String getTitle() {
        return title;
    }

    public DateTime getUtcReleaseDate() {
        return utcReleaseDate;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<UploadedFile> getUploadedFiles() {
        return uploadedFiles;
    }

    public void setUploadedFiles(List<UploadedFile> uploadedFiles) {
        this.uploadedFiles = uploadedFiles;
    }

    public UploadedFile getFeaturedImage() {
        return featuredImage;
    }

    public void setFeaturedImage(UploadedFile featuredImage) {
        this.featuredImage = featuredImage;
    }

    public void removeUploadedFile(UploadedFile file) {
        this.getUploadedFiles().remove(file);
    }

    public PostStatus getPostStatus() {
        return postStatus;
    }

    public void setPostStatus(PostStatus postStatus) {
        this.postStatus = postStatus;
        postStatus.getPosts().add(this);
    }

    public PostType getPostType() {
        return postType;
    }

    public void setPostType(PostType postType) {
        this.postType = postType;
    }

    public void removeFeaturedImage() {
        this.featuredImage = null;
    }

    public boolean isMultipartPost() {
        return isMultipartPost;
    }

    public void setIsMultipartPost(boolean isMultipartPost) {
        this.isMultipartPost = isMultipartPost;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }


    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    public String getShortIntroduction() {
        return shortIntroduction;
    }

    public void setShortIntroduction(String shortIntroduction) {
        this.shortIntroduction = shortIntroduction;
    }

    public PostSubtype getPostSubtype() {
        return postSubtype;
    }

    public void setPostSubtype(PostSubtype postSubtype) {
        this.postSubtype = postSubtype;
    }
}
