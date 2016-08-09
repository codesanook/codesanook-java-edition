package com.codesanook.model;

import javax.persistence.*;

@Entity()
@Table(name = "uploaded_files")
@Access(value = AccessType.FIELD)
public class UploadedFile implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int contextReferenceId;
    private String name;
    private String relativePath;

    private int width;
    private int height;


    public int getId() {
        return id;
    }

    public  void setId (int id){
       this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getContextReferenceId() {
        return contextReferenceId;
    }

    public void setContextReferenceId(int contextReferenceId) {
        this.contextReferenceId = contextReferenceId;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }
}
