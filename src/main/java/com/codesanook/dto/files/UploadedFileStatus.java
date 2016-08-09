package com.codesanook.dto.files;

public class UploadedFileStatus {

    private int statusCode;
    private int uploadedFileId;
    private String statusAndIdKeyPair;

    public UploadedFileStatus(int statusCode, int uploadedFileId) {
        this.statusCode = statusCode;
        this.uploadedFileId = uploadedFileId;
        this.statusAndIdKeyPair = String.format("%d:%d", this.statusCode, this.uploadedFileId);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getUploadedFileId() {
        return uploadedFileId;
    }

    public void setUploadedFileId(int uploadedFileId) {
        this.uploadedFileId = uploadedFileId;
    }

    public String getStatusAndIdKeyPair() {
        return statusAndIdKeyPair;
    }

    public void setStatusAndIdKeyPair(String statusAndIdKeyPair) {
        this.statusAndIdKeyPair = statusAndIdKeyPair;
    }
}
