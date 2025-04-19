package com.aiprompt.sharing.service.impl;

import com.aiprompt.sharing.config.MinioConfig;
import com.aiprompt.sharing.service.FileService;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 文件服务实现类
 */
@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private MinioClient minioClient;
    
    @Autowired
    private MinioConfig minioConfig;

    @Override
    public String uploadFile(MultipartFile file, String directory) throws Exception {
        // 检查文件是否为空
        if (file.isEmpty()) {
            throw new RuntimeException("文件不能为空");
        }
        
        // 检查文件类型
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new RuntimeException("只支持上传图片文件");
        }
        
        // 生成文件名
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String filename = UUID.randomUUID().toString().replaceAll("-", "") + extension;
        String objectName = directory + "/" + filename;
        
        // 检查存储桶是否存在，不存在则创建
        boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder()
                .bucket(minioConfig.getBucketName())
                .build());
        if (!bucketExists) {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(minioConfig.getBucketName())
                    .build());
        }
        
        // 上传文件
        try (InputStream inputStream = file.getInputStream()) {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(minioConfig.getBucketName())
                    .object(objectName)
                    .stream(inputStream, file.getSize(), -1)
                    .contentType(contentType)
                    .build());
        }
        
        return objectName;
    }

    @Override
    public String getFileUrl(String objectName) throws Exception {
        // 生成临时访问URL
        String url = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                .bucket(minioConfig.getBucketName())
                .object(objectName)
                .method(Method.GET)
                .expiry(7, TimeUnit.DAYS)
                .build());
        
        return url;
    }

    @Override
    public void deleteFile(String objectName) throws Exception {
        minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket(minioConfig.getBucketName())
                .object(objectName)
                .build());
    }
}
