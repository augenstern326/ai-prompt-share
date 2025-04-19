package com.aiprompt.sharing.controller;

import com.aiprompt.sharing.common.Result;
import com.aiprompt.sharing.service.FileService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
/**
 * 文件控制器
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;

    /**
     * 上传图片
     */
    @PostMapping("/upload")
    public Result<?> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("directory") String directory) {
        try {
            String objectName = fileService.uploadFile(file, directory);
            String fileUrl = fileService.getFileUrl(objectName);
            Map<String,String> fileInfo = new HashMap<>();
            fileInfo.put("url", fileUrl);
            return Result.success(fileInfo);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取图片URL
     */
    @GetMapping("/url")
    public Result<?> getFileUrl(@RequestParam("objectName") String objectName) {
        try {
            String fileUrl = fileService.getFileUrl(objectName);
            Map<String,String> fileInfo = new HashMap<>();
            fileInfo.put("url", fileUrl);
            return Result.success(fileInfo);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 删除图片
     */
    @DeleteMapping("/delete")
    @RequiresAuthentication
    public Result<?> deleteFile(@RequestParam("objectName") String objectName) {
        try {
            fileService.deleteFile(objectName);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
