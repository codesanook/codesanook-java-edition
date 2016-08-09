package com.codesanook.service;

public class FileFolder {
    private String fileName;
    private  String folderName;

    public FileFolder(String folderName, String fileName){

        this.folderName = folderName;
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }
}
