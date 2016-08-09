package com.codesanook.dto;

import org.joda.time.DateTime;

/**
 * Created by SciMeta on 3/24/2016.
 */
public class NotificationGetRequest {

    private DateTime lastRead;

    public DateTime getLastRead() {
        return lastRead;
    }

    public void setLastRead(DateTime lastRead) {
        this.lastRead = lastRead;
    }
}
