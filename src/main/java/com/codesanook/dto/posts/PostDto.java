package com.codesanook.dto.posts;

import com.codesanook.dto.UrlDto;
import com.codesanook.dto.users.UserDto;
import com.codesanook.model.UploadedFile;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class PostDto {

    private int id;
    private String cacheKey;
    private UserDto user;
    private String title;
    private String alias;
    private String shortIntroduction;
    private String content;
    private String htmlContent;
    private DateTime utcReleaseDate;
    private int pageViewCount;
    private List<String> tags;
    private DateTime utcCreateDate;
    private DateTime utcLastUpdate;
    private List<CommentDto> comments;
    private List<UploadedFileDto> uploadedFiles;
    private UploadedFileDto featuredImage;
    private PostStatusDto postStatus;
    private PostTypeDto postType;
    private PostSubtypeDto postSubtype;

    private boolean isMultipartPost;
    private boolean isDeleted;

    public PostDto() {
        uploadedFiles = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public DateTime getUtcReleaseDate() {
        return utcReleaseDate;
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

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
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

    public List<CommentDto> getComments() {
        return comments;
    }

    public void setComments(List<CommentDto> comments) {
        this.comments = comments;
    }

    public void setFeaturedImage(UploadedFileDto file) {
        this.featuredImage = file;
    }

    public UploadedFileDto getFeaturedImage() {
        return featuredImage;
    }

    public List<UploadedFileDto> getUploadedFiles() {
        return uploadedFiles;
    }

    public void setUploadedFiles(List<UploadedFileDto> uploadedFiles) {
        this.uploadedFiles = uploadedFiles;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }


    public String getCacheKey() {
        return cacheKey;
    }

    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }

    public String getShortIntroduction() {
        return shortIntroduction;
    }

    public void setShortIntroduction(String shortIntroduction) {
        this.shortIntroduction = shortIntroduction;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    public PostStatusDto getPostStatus() {
        return postStatus;
    }

    public void setPostStatus(PostStatusDto postStatus) {
        this.postStatus = postStatus;
    }

    public PostTypeDto getPostType() {
        return postType;
    }

    public void setPostType(PostTypeDto postType) {
        this.postType = postType;
    }


    @JsonProperty("isMultipartPost")
    public boolean isMultipartPost() {
        return isMultipartPost;
    }

    @JsonProperty("isMultipartPost")
    public void setIsMultipartPost(boolean isMultipartPost) {
        this.isMultipartPost = isMultipartPost;
    }

    @JsonProperty("isDeleted")
    public boolean isDeleted() {
        return isDeleted;
    }


    @JsonProperty("isDeleted")
    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public PostSubtypeDto getPostSubtype() {
        return postSubtype;
    }

    public void setPostSubtype(PostSubtypeDto postSubtype) {
        this.postSubtype = postSubtype;
    }
}
