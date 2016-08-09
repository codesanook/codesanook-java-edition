package com.codesanook.dto.users;
import java.util.ArrayList;
import java.util.List;
public class UserProfileDto {

    private int id;
    private String name;
    private String email;
    private List<UserRoleDto> roles;
    private boolean isActivated;
    private boolean isDeleted;
    private String profileImageUrl;

    public UserProfileDto() {
        roles = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<UserRoleDto> getRoles() {
        return roles;
    }

    public void setRoles(List<UserRoleDto> roles) {
        this.roles = roles;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public boolean getIsActivated() {
        return isActivated;
    }

    public void setIsActivated(boolean isActivated) {
        this.isActivated = isActivated;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
