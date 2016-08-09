package com.codesanook.dto.users;

public class FacebookUserDto {

    private String name;
    private String email;
    private String facebookAccessToken;
    private long facebookAppScopeUserId;
    private String profileUrl;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFacebookAccessToken() {
        return facebookAccessToken;
    }

    public void setFacebookAccessToken(String facebookAccessToken) {
        this.facebookAccessToken = facebookAccessToken;
    }

    public long getFacebookAppScopeUserId() {
        return facebookAppScopeUserId;
    }

    public void setFacebookAppScopeUserId(long facebookAppScopeUserId) {
        this.facebookAppScopeUserId = facebookAppScopeUserId;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

