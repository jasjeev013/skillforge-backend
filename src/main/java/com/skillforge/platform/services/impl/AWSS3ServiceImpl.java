package com.skillforge.platform.services.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillforge.platform.services.AWSS3Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.time.Duration;
import java.util.UUID;

@Service
public class AWSS3ServiceImpl implements AWSS3Service {

    private final S3Client s3Client;
    private final ObjectMapper objectMapper;
    private final S3Presigner s3Presigner;

    @Value("${aws.bucket.name}")
    private String bucketName;

    @Value("${aws.s3.public-url-expiration-hours}")
    private long expirationHours;

    public AWSS3ServiceImpl(S3Client s3Client, ObjectMapper objectMapper, S3Presigner s3Presigner) {
        this.s3Client = s3Client;
        this.objectMapper = objectMapper;
        this.s3Presigner = s3Presigner;
    }

    public String uploadPdf(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be null or empty");
        }

        // Generate unique key under /pdfs/
        String key = "pdfs/" + UUID.randomUUID() + ".pdf";

        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .contentType("application/pdf")
                        .build(),
                RequestBody.fromBytes(file.getBytes())
        );

        // Return presigned or public URL
        return generatePublicUrl(key);
    }

    public String uploadVideo(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be null or empty");
        }

        // Detect file extension (default .mp4)
        String extension = ".mp4";
        String originalFilename = file.getOriginalFilename();

        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        // Generate key under /videos/
        String key = "videos/" + UUID.randomUUID() + extension;

        // Infer video MIME type (basic detection)
        String contentType = "video/mp4";
        if (extension.equalsIgnoreCase(".mov")) contentType = "video/quicktime";
        else if (extension.equalsIgnoreCase(".avi")) contentType = "video/x-msvideo";
        else if (extension.equalsIgnoreCase(".webm")) contentType = "video/webm";

        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .contentType(contentType)
                        .build(),
                RequestBody.fromBytes(file.getBytes())
        );

        return generatePublicUrl(key);
    }

    public void deleteFile(String s3Url) {
        String key = extractKeyFromUrl(s3Url);

        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build());
    }


    private String generatePublicUrl(String key) {
        if (expirationHours > 0) {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofHours(expirationHours))
                    .getObjectRequest(getObjectRequest)
                    .build();

            return s3Presigner.presignGetObject(presignRequest).url().toString();
        }

        return s3Client.utilities().getUrl(builder -> builder.bucket(bucketName).key(key)).toString();
    }

    public String uploadImage(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be null or empty");
        }

        // Get the file extension and determine the content type
        String extension = ".jpg"; // default extension
        String originalFilename = file.getOriginalFilename();

        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        // Generate key under /images/
        String key = "images/" + UUID.randomUUID() + extension;

        // Infer image MIME type (basic detection)
        String contentType = "image/jpeg"; // default content type
        if (extension.equalsIgnoreCase(".png")) contentType = "image/png";
        else if (extension.equalsIgnoreCase(".gif")) contentType = "image/gif";
        else if (extension.equalsIgnoreCase(".bmp")) contentType = "image/bmp";
        else if (extension.equalsIgnoreCase(".webp")) contentType = "image/webp";

        // Upload the image file to S3
        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .contentType(contentType)
                        .build(),
                RequestBody.fromBytes(file.getBytes())
        );

        // Return presigned or public URL
        return generatePublicUrl(key);
    }
    public String uploadImage(File file) throws IOException {
        if (file == null || !file.exists() || file.length() == 0) {
            throw new IllegalArgumentException("File cannot be null, missing, or empty");
        }

        // Extract extension
        String name = file.getName();
        String extension = ".jpg"; // default

        if (name.contains(".")) {
            extension = name.substring(name.lastIndexOf("."));
        }

        // Generate S3 key
        String key = "images/" + UUID.randomUUID() + extension;

        // Detect content type
        String contentType = Files.probeContentType(file.toPath());
        if (contentType == null) {
            contentType = "image/jpeg";
        }

        // Upload file to S3
        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .contentType(contentType)
                        .build(),
                RequestBody.fromFile(file)
        );

        // Return public URL
        return generatePublicUrl(key);
    }

    private String extractKeyFromUrl(String url) {
        try {
            URL s3Url = new URL(url);
            return s3Url.getPath().replaceFirst("^/", "");
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid S3 URL format", e);
        }
    }

    public byte[] downloadFile(String key) {
        ResponseBytes<GetObjectResponse> objectAsBytes = s3Client.getObjectAsBytes(GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build());
        return objectAsBytes.asByteArray();
    }

}