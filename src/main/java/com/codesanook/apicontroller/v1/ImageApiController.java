package com.codesanook.apicontroller.v1;

import com.codesanook.dto.files.ImageEditRequest;
import com.codesanook.dto.files.UploadedFileStatus;
import com.codesanook.dto.posts.UploadedFileDto;
import com.codesanook.interceptor.Authorize;
import com.codesanook.model.UploadedFile;
import com.codesanook.service.FileService;
import com.mangofactory.swagger.annotations.ApiIgnore;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.security.GeneralSecurityException;

@Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_UNCOMMITTED)
@RestController
@RequestMapping("/api/images")
public class ImageApiController {


    private Log log = LogFactory.getLog(ImageApiController.class);
    private FileService fileService;

    @Autowired
    public ImageApiController(FileService fileService) {
        this.fileService = fileService;
    }


    @Authorize
    @RequestMapping(value = "/{fileId}", method = RequestMethod.GET)
    public UploadedFileDto getImage(@PathVariable int fileId){
        UploadedFileDto uploadedFileDto = fileService.toUploadedFileDto(fileService.getUploadedFile(fileId));
        return uploadedFileDto;
    }


    @Authorize
    @RequestMapping(value = "/{fileId}", method = RequestMethod.PUT)
    public UploadedFileDto editImage(@PathVariable int fileId, @RequestBody ImageEditRequest imageEditRequest)
            throws IOException, GeneralSecurityException, InterruptedException {
        UploadedFileDto uploadedFileDto = fileService.toUploadedFileDto(fileService.editImage(fileId, imageEditRequest));
        return uploadedFileDto;
    }


    @ApiIgnore
    @Authorize
    @RequestMapping(value = "/upload-image", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public UploadedFileStatus uploadImage(@RequestParam("file") MultipartFile file,
                                          @RequestParam("width") int width,
                                          @RequestParam("height") int height) throws IOException, GeneralSecurityException, InterruptedException {

        log.debug(String.format("width=%d,height=%d", width, height));
        UploadedFile uploadedFile = fileService.addUploadedFile(file, width, height);
        UploadedFileStatus status = new UploadedFileStatus(200, uploadedFile.getId());
        return status;
    }



    @Authorize
    @RequestMapping(value = "/{fileId}", method = RequestMethod.DELETE)
    public void deleteImage(@PathVariable int fileId, @RequestBody ImageEditRequest imageEditRequest)
            throws IOException, GeneralSecurityException {
        fileService.deleteUploadedFile(fileId);
    }

}


