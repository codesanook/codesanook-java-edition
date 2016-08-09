package com.codesanook.dto.users;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;
public class UserRegisterRequest {


    @NotEmpty
    @Email
    private String email;

    @Size(min = 3, max = 20)
    private String name;

    @Size(min = 6, max = 20)
    private String password;

    @Size(min = 6, max = 20)
    private  String confirmPassword;



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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }



    public boolean validatePaswordMatch() {
       return this.password.equals(this.confirmPassword);
    }

}
