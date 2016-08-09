package com.codesanook.dto.users;

/**
 * Created by SciMeta on 12/3/2015.
 */
public class LoggedInUserDto {
    private  int id;
    private String name;
    private  String profileUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }
}
