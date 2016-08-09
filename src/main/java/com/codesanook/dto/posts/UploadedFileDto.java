package com.codesanook.dto.posts;

import com.codesanook.model.UploadedFile;

public class UploadedFileDto extends UploadedFile {
    private  String absoluteUrl;

    public String getAbsoluteUrl() {
        return absoluteUrl;
    }

    public void setAbsoluteUrl(String absoluteUrl) {
        this.absoluteUrl = absoluteUrl;
    }
}
