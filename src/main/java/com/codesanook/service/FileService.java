package com.codesanook.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.event.ProgressEvent;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.transfer.PersistableTransfer;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import com.amazonaws.services.s3.transfer.internal.S3ProgressListener;
import com.codesanook.dto.files.ImageEditRequest;
import com.codesanook.dto.posts.UploadedFileDto;
import com.codesanook.model.UploadedFile;
import com.codesanook.repository.FileRepository;
import com.codesanook.util.HashUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.imgscalr.Scalr;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.FileNameMap;
import java.net.URL;
import java.net.URLConnection;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class FileService {

    private Log log = LogFactory.getLog(FileService.class);
    private final String FILE_ROOT_FOLDER = "uploaded";

    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    @Value("${aws.access.key.id}")
    private String awsAccessKeyId;

    @Value("${aws.secret.access.key}")
    private String awsSecretAccessKey;

    @Value("${aws.s3.root.url}")
    private String rootUrl;

    private FileRepository fileRepository;
    private static final Map<String, String> fileExtensionMap;

    static {
        fileExtensionMap = new HashMap<String, String>();
        // MS Office
        fileExtensionMap.put("doc", "application/msword");
        fileExtensionMap.put("dot", "application/msword");
        fileExtensionMap.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        fileExtensionMap.put("dotx", "application/vnd.openxmlformats-officedocument.wordprocessingml.template");
        fileExtensionMap.put("docm", "application/vnd.ms-word.document.macroEnabled.12");
        fileExtensionMap.put("dotm", "application/vnd.ms-word.template.macroEnabled.12");
        fileExtensionMap.put("xls", "application/vnd.ms-excel");
        fileExtensionMap.put("xlt", "application/vnd.ms-excel");
        fileExtensionMap.put("xla", "application/vnd.ms-excel");
        fileExtensionMap.put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        fileExtensionMap.put("xltx", "application/vnd.openxmlformats-officedocument.spreadsheetml.template");
        fileExtensionMap.put("xlsm", "application/vnd.ms-excel.sheet.macroEnabled.12");
        fileExtensionMap.put("xltm", "application/vnd.ms-excel.template.macroEnabled.12");
        fileExtensionMap.put("xlam", "application/vnd.ms-excel.addin.macroEnabled.12");
        fileExtensionMap.put("xlsb", "application/vnd.ms-excel.sheet.binary.macroEnabled.12");
        fileExtensionMap.put("ppt", "application/vnd.ms-powerpoint");
        fileExtensionMap.put("pot", "application/vnd.ms-powerpoint");
        fileExtensionMap.put("pps", "application/vnd.ms-powerpoint");
        fileExtensionMap.put("ppa", "application/vnd.ms-powerpoint");
        fileExtensionMap.put("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");
        fileExtensionMap.put("potx", "application/vnd.openxmlformats-officedocument.presentationml.template");
        fileExtensionMap.put("ppsx", "application/vnd.openxmlformats-officedocument.presentationml.slideshow");
        fileExtensionMap.put("ppam", "application/vnd.ms-powerpoint.addin.macroEnabled.12");
        fileExtensionMap.put("pptm", "application/vnd.ms-powerpoint.presentation.macroEnabled.12");
        fileExtensionMap.put("potm", "application/vnd.ms-powerpoint.presentation.macroEnabled.12");
        fileExtensionMap.put("ppsm", "application/vnd.ms-powerpoint.slideshow.macroEnabled.12");
        // Open Office
        fileExtensionMap.put("odt", "application/vnd.oasis.opendocument.text");
        fileExtensionMap.put("ott", "application/vnd.oasis.opendocument.text-template");
        fileExtensionMap.put("oth", "application/vnd.oasis.opendocument.text-web");
        fileExtensionMap.put("odm", "application/vnd.oasis.opendocument.text-master");
        fileExtensionMap.put("odg", "application/vnd.oasis.opendocument.graphics");
        fileExtensionMap.put("otg", "application/vnd.oasis.opendocument.graphics-template");
        fileExtensionMap.put("odp", "application/vnd.oasis.opendocument.presentation");
        fileExtensionMap.put("otp", "application/vnd.oasis.opendocument.presentation-template");
        fileExtensionMap.put("ods", "application/vnd.oasis.opendocument.spreadsheet");
        fileExtensionMap.put("ots", "application/vnd.oasis.opendocument.spreadsheet-template");
        fileExtensionMap.put("odc", "application/vnd.oasis.opendocument.chart");
        fileExtensionMap.put("odf", "application/vnd.oasis.opendocument.formula");
        fileExtensionMap.put("odb", "application/vnd.oasis.opendocument.database");
        fileExtensionMap.put("odi", "application/vnd.oasis.opendocument.image");
        fileExtensionMap.put("oxt", "application/vnd.openofficeorg.extension");
        // Other
        fileExtensionMap.put("txt", "text/plain");
        fileExtensionMap.put("rtf", "application/rtf");
        fileExtensionMap.put("pdf", "application/pdf");
    }

    private String[] supportedFiles = {"jpeg", "png", "jpg"};

    public static final String QUERY_CHAR = "?";
    public static final String ANCHOR_CHAR = "#";

    @Autowired
    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }


    private String getRandomNewFileName(String fileName) {
        String fileExtension = getFileExtension(fileName);
        DateTime utcNow = DateTime.now(DateTimeZone.UTC);
        String newFileName = utcNow.getMillis() + "-" + HashUtils.generatePassword(12) + "." + fileExtension;
        return newFileName;
    }

    public String getMimeType(String fileName) {
        // 1. first use java's built-in utils
        FileNameMap mimeTypes = URLConnection.getFileNameMap();
        String contentType = mimeTypes.getContentTypeFor(fileName);

        // 2. nothing found -> lookup our in extension map to find types like ".doc" or ".docx"
        if (contentType == null) {
            String extension = getFileExtension(fileName);

            contentType = fileExtensionMap.get(extension);
        }
        return contentType;
    }


    public UploadedFile getUploadedFile(int fileId) {
        return fileRepository.getFileById(fileId);
    }

    private String removeQueryString(String url) {
        int queryPosition = url.indexOf(QUERY_CHAR);
        if (queryPosition <= 0) queryPosition = url.indexOf(ANCHOR_CHAR);
        if (queryPosition >= 0) url = url.substring(0, queryPosition);
        return url;
    }

    public UploadedFile addUploadedFile(String fileUrl)
            throws IOException, GeneralSecurityException, InterruptedException {


        InputStream inputStream = new URL(fileUrl).openStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int read = 0;
        while ((read = inputStream.read(buffer, 0, buffer.length)) > 0) {
            baos.write(buffer, 0, read);
        }
        byte[] fileData = baos.toByteArray();
        inputStream.close();
        baos.close();

        String safeUrl = removeQueryString(fileUrl);
        String fileExtension = getFileExtension(safeUrl);
        if (!isSupportedFile(fileExtension)) throw
                new IllegalStateException(String.format("file extension %s not supported", fileExtension));


        DateTime utcNow = DateTime.now(DateTimeZone.UTC);
        String newFileName = getRandomNewFileName(safeUrl);
        String contentType = getMimeType(newFileName);
        String relativePath = String.format("%s/%s/%s/%s/%s",
                FILE_ROOT_FOLDER, utcNow.getYear(), utcNow.getMonthOfYear(),
                utcNow.getDayOfMonth(), newFileName);

        ByteArrayInputStream fileInputStream = new ByteArrayInputStream(fileData);
        uploadFileToS3Multipart(fileInputStream, contentType, fileData.length, relativePath);
        UploadedFile uploadedFile = new UploadedFile();
        uploadedFile.setName(newFileName);
        uploadedFile.setRelativePath(relativePath);

        BufferedImage original = byteArrayToBufferedImage(fileData);
        uploadedFile.setWidth(original.getWidth());
        uploadedFile.setHeight(original.getHeight());
        fileRepository.addFile(uploadedFile);
        return uploadedFile;
    }


    public UploadedFile addUploadedFile(MultipartFile file, int maxWidth, int maxHeight)
            throws IOException, GeneralSecurityException, InterruptedException {
        if (file.isEmpty()) throw new IllegalStateException("file is empty");

        String fileName = file.getOriginalFilename();
        fileName = fileName.replaceAll("\\s+", "");
        String fileExtension = getFileExtension(fileName);

        if (!isSupportedFile(fileExtension)) throw
                new IllegalStateException(String.format("file extension %s not supported", fileExtension));


        byte[] fileData = file.getBytes();
        BufferedImage original = byteArrayToBufferedImage(fileData);

        BufferedImage bufferedImageResult;
        InputStream inputStreamResult;
        byte[] fileDataResult;

        if (maxWidth >= original.getWidth()) {
            bufferedImageResult = original;
            inputStreamResult = new ByteArrayInputStream(fileData);
            fileDataResult = fileData;
        } else {
            BufferedImage resizedImage = resizeImage(original, maxWidth, maxHeight);
            bufferedImageResult = resizedImage;
            byte[] fileBytes = bufferedToByteArraySimple(resizedImage, fileExtension);
            inputStreamResult = new ByteArrayInputStream(fileBytes);
            fileDataResult = fileBytes;
        }

        DateTime utcNow = DateTime.now(DateTimeZone.UTC);
        String newFileName = utcNow.getMillis() + "-" + fileName;
        String contentType = getMimeType(fileName);
        String relativePath = String.format("%s/%s/%s/%s/%s",
                FILE_ROOT_FOLDER, utcNow.getYear(), utcNow.getMonthOfYear(),
                utcNow.getDayOfMonth(), newFileName);

        uploadFileToS3Multipart(inputStreamResult, contentType, fileDataResult.length, relativePath);
        UploadedFile uploadedFile = new UploadedFile();
        uploadedFile.setName(newFileName);
        uploadedFile.setRelativePath(relativePath);
        uploadedFile.setWidth(bufferedImageResult.getWidth());
        uploadedFile.setHeight(bufferedImageResult.getHeight());
        fileRepository.addFile(uploadedFile);
        return uploadedFile;
    }

    public BufferedImage byteArrayToBufferedImage(byte[] imageInByte) throws IOException {
        InputStream in = new ByteArrayInputStream(imageInByte);
        BufferedImage bImageFromConvert = ImageIO.read(in);
        in.close();
        return bImageFromConvert;
    }


    public FileFolder getFileFolder(String relativePath) {

        String regex = "([\\w/]+)/(.+\\.[\\w]+)$"; //match all file name
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(relativePath);
        while (m.find()) {
            String folder = m.group(1);
            String fileName = m.group(2).replaceAll("\\s+", "");

            log.debug(String.format("folder %s, fileName %s", folder, fileName));
            FileFolder fileFolder = new FileFolder(folder, fileName);
            return fileFolder;
        }
        return null;
    }


    public boolean isSupportedFile(String fileExtension) {
        fileExtension = fileExtension.toLowerCase();
        for (String support : supportedFiles) {
            if (fileExtension.equals(support)) return true;
        }

        return false;
    }


    public void uploadFileToS3Multipart(InputStream inputStream,
                                        String contentType,
                                        long fileSize, String relativePath)
            throws IOException, GeneralSecurityException, InterruptedException {

        AWSCredentials credentials = new BasicAWSCredentials(awsAccessKeyId, awsSecretAccessKey);
        TransferManager tm = new TransferManager(credentials);
        FileFolder fileFolder = getFileFolder(relativePath);
        ObjectMetadata omd = new ObjectMetadata();
        omd.setContentType(contentType);
        omd.setContentLength(fileSize);
        omd.setHeader("filename", fileFolder.getFileName());
        String key = relativePath;
        PutObjectRequest request = new PutObjectRequest(bucketName, key, inputStream, omd);
        request.withCannedAcl(CannedAccessControlList.PublicRead);
        Upload upload = tm.upload(request, new S3ProgressListener() {
            @Override
            public void onPersistableTransfer(PersistableTransfer persistableTransfer) {

            }

            @Override
            public void progressChanged(ProgressEvent progressEvent) {
                log.debug(String.format("bytesTransferred %d", progressEvent.getBytesTransferred()));
            }
        });

        try {
            upload.waitForCompletion();
        } catch (AmazonClientException amazonClientException) {
            log.error(amazonClientException);
            amazonClientException.printStackTrace();
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    public String getFileExtension(String fileName) {
        String extension = "";
        int i = fileName.lastIndexOf('.');
        if (i >= 0) {
            extension = fileName.substring(i + 1).toLowerCase();
        }
        log.debug(String.format("extension %s", extension));
        return extension;
    }

    public void deleteUploadedFile(int fileId) {

        UploadedFile uploadedFile = fileRepository.getFileById(fileId);
        //delete from database
        fileRepository.deleteUploadedFile(uploadedFile);

        //delete from disk
        if (!deleteFileFromS3(uploadedFile.getRelativePath())) {
            throw new IllegalStateException(String.format("cannot delete fileId %s", uploadedFile.getId()));
        }
    }

    private boolean deleteFileFromS3(String relativePath) {

        try {
            AWSCredentials credentials = new BasicAWSCredentials(awsAccessKeyId, awsSecretAccessKey);
            AmazonS3 s3client = new AmazonS3Client(credentials);
            s3client.deleteObject(bucketName, relativePath);
            return true;
        } catch (Exception ex) {
            log.error(ex);
            return false;
        }
    }


    public BufferedImage resizeImage(BufferedImage originalImage, int newWidth, int newHeight) {

        //not resize if new width bigger than original
        if (newWidth >= originalImage.getWidth()) return originalImage;

        BufferedImage thumbnail = Scalr.resize(originalImage, Scalr.Method.ULTRA_QUALITY,
                Scalr.Mode.FIT_TO_WIDTH, newWidth, newHeight);
        return thumbnail;
    }

    public BufferedImage cropImage(BufferedImage originalImage, int cropX, int cropY, int cropWidth, int cropHeight) {
        BufferedImage cropedImage = Scalr.crop(originalImage, cropX, cropY, cropWidth, cropHeight);
        return cropedImage;
    }

    public InputStream getFileData(UploadedFile uploadedFile) throws IOException, GeneralSecurityException {
        AWSCredentials credentials = new BasicAWSCredentials(awsAccessKeyId, awsSecretAccessKey);
        AmazonS3 s3client = new AmazonS3Client(credentials);
        S3Object object = s3client.getObject(
                new GetObjectRequest(bucketName, uploadedFile.getRelativePath()));
        InputStream objectData = object.getObjectContent();
        return objectData;
    }

    public String getAbsoluteUrl(String relativeUrl) {
        log.debug(String.format("rootUrl %s", rootUrl));
        String url = String.format("%s/%s", rootUrl, relativeUrl);
        log.debug(String.format("absoluteUrl %s", url));
        return url;
    }


    public UploadedFile editImage(int fileId, ImageEditRequest imageEditRequest) throws IOException, GeneralSecurityException, InterruptedException {

        //not resize to prevent image quality
        UploadedFile uploadedFile = getUploadedFile(fileId);
        BufferedImage originalImage = getBufferedImage(uploadedFile);
        BufferedImage resizedImage = cropImage(originalImage,
                imageEditRequest.getCropX(),
                imageEditRequest.getCropY(),
                imageEditRequest.getCropWidth(),
                imageEditRequest.getCropHeight());

        String fileExtension = getFileExtension(uploadedFile.getName());
        byte[] fileData = bufferedToByteArraySimple(resizedImage, fileExtension);
        InputStream inputStream = new ByteArrayInputStream(fileData);
        String contentType = getMimeType(uploadedFile.getName());
        uploadFileToS3Multipart(inputStream, contentType, fileData.length, uploadedFile.getRelativePath());

        uploadedFile.setWidth(resizedImage.getWidth());
        uploadedFile.setHeight(resizedImage.getHeight());
        return uploadedFile;
    }

    public BufferedImage getBufferedImage(UploadedFile uploadUploadedFile) throws IOException, GeneralSecurityException {
        InputStream inputStream = getFileData(uploadUploadedFile);
        return getBufferedImage(inputStream);
    }

    public BufferedImage getBufferedImage(InputStream inputStream) throws IOException, GeneralSecurityException {

        BufferedImage bufferedImage = ImageIO.read(inputStream);
        inputStream.close();
        return bufferedImage;
    }

    public byte[] bufferedToByteArraySimple(BufferedImage bufferedImage, String fileExtension) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, fileExtension, baos);
        baos.flush();

        byte[] imageInByte = baos.toByteArray();
        baos.close();
        return imageInByte;
    }

    public UploadedFileDto toUploadedFileDto(UploadedFile uploadedFile) {
        if (uploadedFile == null) return null;
        UploadedFileDto uploadedFileDto = new UploadedFileDto();

        uploadedFileDto.setId(uploadedFile.getId());
        uploadedFileDto.setContextReferenceId(uploadedFile.getContextReferenceId());

        uploadedFileDto.setHeight(uploadedFile.getHeight());
        uploadedFileDto.setWidth(uploadedFile.getWidth());

        String absoluteUrl = getAbsoluteUrl(uploadedFile.getRelativePath());
        uploadedFileDto.setAbsoluteUrl(absoluteUrl);
        uploadedFileDto.setRelativePath(uploadedFile.getRelativePath());

        uploadedFileDto.setName(uploadedFile.getName());

        return uploadedFileDto;
    }


}

