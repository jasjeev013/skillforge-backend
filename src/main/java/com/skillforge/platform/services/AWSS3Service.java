package com.skillforge.platform.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public interface AWSS3Service {

    String uploadPdf(MultipartFile file) throws IOException;
    String uploadVideo(MultipartFile file) throws IOException;
    String uploadImage(MultipartFile file) throws IOException;
    String uploadImage(File file) throws IOException;
    byte[] downloadFile(String key);
    void deleteFile(String s3Url);

}
