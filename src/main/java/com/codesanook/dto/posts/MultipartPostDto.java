package com.codesanook.dto.posts;

import com.codesanook.dto.users.UserDto;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class MultipartPostDto {

    private int id;
    private String title;
    private UserDto user;

    private List<MultipartPostItemDto> multipartPostItems;

    private DateTime utcCreateDate;
    private DateTime utcLastUpdate;


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



    public MultipartPostDto() {
        multipartPostItems = new ArrayList<>();
    }

    public List<MultipartPostItemDto> getMultipartPostItems() {
        return multipartPostItems;
    }

    public void setMultipartPostItems(List<MultipartPostItemDto> multipartPostItems) {
        this.multipartPostItems = multipartPostItems;
    }
}
