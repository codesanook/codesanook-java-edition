package com.codesanook.dto.users;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Required;

public class UserLogInRequest {

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    private String password;

    private String facebookAccessToken;
    private long facebookAppScopeUserId;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFacebookAccessToken() {
        return facebookAccessToken;
    }

    public long getFacebookAppScopeUserId() {
        return facebookAppScopeUserId;
    }

    public void setFacebookAppScopeUserId(long facebookAppScopeUserId) {
        this.facebookAppScopeUserId = facebookAppScopeUserId;
    }

    public void setFacebookAccessToken(String facebookAccessToken) {
        this.facebookAccessToken = facebookAccessToken;
    }
}
