package com.codesanook.dto.users;

/**
 * Created by SciMeta on 11/7/2015.
 */
public class UserForgotPasswordResponse {
    private  String name;
    private  String newPassword;
    private String logInUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getLogInUrl() {
        return logInUrl;
    }

    public void setLogInUrl(String logInUrl) {
        this.logInUrl = logInUrl;
    }
}
