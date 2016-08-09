package com.codesanook.dto.users;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

public class UserForgotPasswordRequest {

    @NotEmpty
    @Email
    private  String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
