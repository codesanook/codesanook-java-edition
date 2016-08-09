package com.codesanook.interceptor;

import com.codesanook.exception.UnauthorizedException;
import com.codesanook.model.RoleEnum;
import org.joda.time.DateTime;
import javax.servlet.http.HttpServletRequest;


public class LoggedInUser {

    private int id;
    private String name;
    private String profileImageUrl;

    private int[] roles;
    private DateTime expired;

    public static final String LOGGED_IN_USER_KEY = "loggedInUser";
    public static final String TOKEN_KEY = "cs-token";

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

    public int[] getRoles() {
        return roles;
    }

    public void setRoles(int[] roles) {
        this.roles = roles;
    }

    public DateTime getExpired() {
        return expired;
    }

    public void setExpired(DateTime expired) {
        this.expired = expired;
    }

    public static LoggedInUser getLogginUser(HttpServletRequest httpRequest) {
        LoggedInUser loggedInUser = (LoggedInUser) httpRequest.getAttribute(LoggedInUser.LOGGED_IN_USER_KEY);
        return loggedInUser;
    }

    public boolean owningResource(int userIdOfResource) {
        return this.getId() == userIdOfResource;
    }

    public void validateIfOwnsResource(int userIdOfResource) {
        if (this.getId() != userIdOfResource) throw new UnauthorizedException();
    }

    public boolean havingRole(RoleEnum roleEnum) {
        for (int roleId : roles) {
            if (roleId == roleEnum.getId()) return true;
        }
        return false;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
