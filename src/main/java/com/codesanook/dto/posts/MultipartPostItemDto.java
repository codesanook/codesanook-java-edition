package com.codesanook.dto.posts;

import com.codesanook.dto.users.UserDto;
import org.joda.time.DateTime;

public class MultipartPostItemDto {

    private PostMetaDataDto post;
    private UserDto user;
    private int orderIndex;
    private DateTime utcCreateDate;
    private DateTime utcLastUpdate;

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
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

    public PostMetaDataDto getPost() {
        return post;
    }

    public void setPost(PostMetaDataDto post) {
        this.post = post;
    }
}
