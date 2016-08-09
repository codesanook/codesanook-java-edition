package com.codesanook.dto.users;

import javax.validation.constraints.Size;

public class UserChangePasswordRequest {

    private int userId;

    @Size(min = 6, max = 20)
    private  String currentPassword;

    @Size(min = 6, max = 20)
    private String newPassword;
    private  String confirmNewPassword;

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    public void setConfirmNewPassword(String confirmNewPassword) {
        this.confirmNewPassword = confirmNewPassword;
    }

    public boolean validateConfirmPasswordMatch() {
        return this.newPassword.equals(this.confirmNewPassword);
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
