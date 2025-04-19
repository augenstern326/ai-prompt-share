package com.aiprompt.sharing.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * 文件服务接口
 */
public interface FileService {

    /**
     * 上传文件
     * @param file 文件
     * @param directory 目录
     * @return 文件对象名
     */
    String uploadFile(MultipartFile file, String directory) throws Exception;

    /**
     * 获取文件URL
     * @param objectName 文件对象名
     * @return 文件URL
     */
    String getFileUrl(String objectName) throws Exception;

    /**
     * 删除文件
     * @param objectName 文件对象名
     */
    void deleteFile(String objectName) throws Exception;
}
