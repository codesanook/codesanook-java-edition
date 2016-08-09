package com.codesanook.dto;

import java.util.ArrayList;
import java.util.List;

public class UserAdminEditRequest {

    private int userId;
    private boolean isDeleted;
    private List<Integer> userRoleIds;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


    public List<Integer> getUserRoleIds() {
        if (userRoleIds == null) return new ArrayList<>();
        return userRoleIds;
    }

    public void setUserRoleIds(List<Integer> userRoleIds) {
        this.userRoleIds = userRoleIds;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
