package com.codesanook.model;

import com.restfb.Facebook;

public class FacebookUser {
    @Facebook
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
