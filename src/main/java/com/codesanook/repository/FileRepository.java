package com.codesanook.repository;

import com.codesanook.model.UploadedFile;
import com.codesanook.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class FileRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public UploadedFile getFileById(int uploadFileId) {

        return entityManager.getReference(UploadedFile.class, uploadFileId);
    }

    public void addFile(UploadedFile uploadedFile) {
        entityManager.persist(uploadedFile);
    }

    public void deleteUploadedFile(UploadedFile uploadedFile) {
        entityManager.remove(uploadedFile);
    }
}

