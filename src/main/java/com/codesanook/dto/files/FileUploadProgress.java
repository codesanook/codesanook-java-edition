package com.codesanook.dto.files;

/**
 * Created by SciMeta on 11/10/2015.
 */
public class FileUploadProgress {

    private long fileWritten;
    private long fileSize;

    public FileUploadProgress(){}
    public FileUploadProgress(long fileWritten,long fileSize) {
        this.fileWritten = fileWritten;
        this.fileSize = fileSize;
    }

    public long getFileWritten() {
        return fileWritten;
    }

    public void setFileWritten(long fileWritten) {
        this.fileWritten = fileWritten;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }
}
