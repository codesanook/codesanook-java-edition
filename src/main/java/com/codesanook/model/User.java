package com.codesanook.model;

import com.codesanook.exception.UnauthorizedException;
import com.codesanook.util.HashUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "users")
public class User {

    @Transient
    public final static String DEFAULT_PROFILE_URL = "/img/icon-user-placeholder.png";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    @Column(unique = true,nullable = false)
    private String email;
    private boolean isActivated;
    private String activationCode;
    private boolean isDeleted;
    private DateTime utcCreateDate;
    private DateTime utcLastUpdate;
    private String password;
    private String facebookAccessToken;
    private long facebookAppScopeUserId;

    @ManyToOne
    @JoinColumn(name = "uploaded_file_id", nullable = true,
            foreignKey = @ForeignKey(name = "FK_user_uploaded_file_id"))
    private UploadedFile profileImage;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.LAZY)
    private List<Post> posts;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = true),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = true))
    private List<Role> roles;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.LAZY)
    private List<Comment> comments;


//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.EAGER)
//    private List<UserEmail> emails;

    public User() {
        roles = new ArrayList<>();
        comments = new ArrayList<>();
        posts = new ArrayList<>();
    }


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

    public int getId() {
        return id;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public void addRole(Role role) {
        role.getUsers().add(this);
        this.getRoles().add(role);
    }


    public void validateRequriedProperties() {
        if (StringUtils.isEmpty(name))
            throw new IllegalStateException("Name required");
        if (StringUtils.isEmpty(email))
            throw new IllegalStateException("Email required");
//        if (StringUtils.isEmpty(password))
//            throw new IllegalStateException("Password required");
    }


    public void validatePassword(String plainTextPassword) {
        String cryptoPassword = HashUtils.md5Hex(plainTextPassword);
        if (!password.equals(cryptoPassword))
            throw new UnauthorizedException("wrong current password");
    }

    public void setUserAlreadyActivated() {
        isActivated = true;
    }

    public void blockUser() {
        isDeleted = true;
    }

    public void unblockUser() {
        isDeleted = false;
    }

    public void setPassword(String password) {
        if (StringUtils.isEmpty(password))
            throw new IllegalStateException("password is empty");

        String cryptoPassword = HashUtils.md5Hex(password);
        this.password = cryptoPassword;
    }

    public String resetPassword() {
        String randomPassword = HashUtils.generatePassword(6);
        //save new password
        String hashPassword = HashUtils.md5Hex(randomPassword);
        password = hashPassword;

        return randomPassword;
    }

    public void changePassword(String currentPassword, String newPassword) {
        validatePassword(currentPassword);
        String hashPassword = HashUtils.md5Hex(newPassword);
        password = hashPassword;
    }

    public void addRoles(Role[] roles) {
        for (Role role : roles) {
            addRole(role);
        }
    }

    public boolean doesHaveAtLeastOneRole(String[] roleNames) {

        for (String roleName : roleNames) {
            for (Role role : roles) {
                if (role.getName().toUpperCase().equals(roleName.toUpperCase())) {
                    return true;
                }
            }
        }

        return false;
    }


    public void removeRole(Role role) {
        roles.remove(role);
        role.getUsers().remove(this);
    }


    public void removeRoles(Role[] roles) {
        for (Role role : roles) {
            removeRole(role);
        }
    }

    public String getPassword() {
        return password;
    }

    public boolean getIsActivated() {
        return isActivated;
    }

    public void setIsActivated(boolean isActivated) {
        this.isActivated = isActivated;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
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

    public UploadedFile getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(UploadedFile profileImage) {
        this.profileImage = profileImage;
    }

    public void newActivationCode() {
        String randomCode = HashUtils.generatePassword(12);
        this.activationCode = randomCode;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
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

}
