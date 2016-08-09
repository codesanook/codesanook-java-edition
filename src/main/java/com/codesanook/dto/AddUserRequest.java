package com.codesanook.dto;

/**
 * Created by SciMeta on 9/4/2015.
 */
public class AddUserRequest {
    private  String name;
    private  String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
