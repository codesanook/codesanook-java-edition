package com.codesanook.dto;

import org.joda.time.DateTime;

/**
 * Created by SciMeta on 3/24/2016.
 */
public class NewNotificationDto {

    private int totalCount;
    private DateTime lastRead;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public DateTime getLastRead() {
        return lastRead;
    }

    public void setLastRead(DateTime lastRead) {
        this.lastRead = lastRead;
    }
}
