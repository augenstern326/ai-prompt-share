package com.aiprompt.sharing.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 文件服务测试类
 */
@SpringBootTest
@Transactional
public class FileServiceTest {

    @Autowired
    private FileService fileService;

    @Test
    public void testUploadAndGetFileUrl() throws Exception {
        // 创建模拟的图片文件
        byte[] content = new byte[]{1, 2, 3, 4, 5};
        MockMultipartFile file = new MockMultipartFile(
                "test.jpg",
                "test.jpg",
                "image/jpeg",
                content
        );
        
        String directory = "test";
        
        // 测试上传文件
        String objectName = fileService.uploadFile(file, directory);
        
        assertNotNull(objectName);
        assertTrue(objectName.startsWith(directory + "/"));
        assertTrue(objectName.endsWith(".jpg"));
        
        // 测试获取文件URL
        String fileUrl = fileService.getFileUrl(objectName);
        
        assertNotNull(fileUrl);
        assertTrue(fileUrl.contains(objectName));
    }

    @Test
    public void testUploadInvalidFile() {
        // 创建非图片文件
        byte[] content = new byte[]{1, 2, 3, 4, 5};
        MockMultipartFile file = new MockMultipartFile(
                "test.txt",
                "test.txt",
                "text/plain",
                content
        );
        
        String directory = "test";
        
        // 测试上传非图片文件，应该抛出异常
        assertThrows(RuntimeException.class, () -> {
            fileService.uploadFile(file, directory);
        });
    }

    @Test
    public void testUploadEmptyFile() {
        // 创建空文件
        byte[] content = new byte[0];
        MockMultipartFile file = new MockMultipartFile(
                "empty.jpg",
                "empty.jpg",
                "image/jpeg",
                content
        );
        
        String directory = "test";
        
        // 测试上传空文件，应该抛出异常
        assertThrows(RuntimeException.class, () -> {
            fileService.uploadFile(file, directory);
        });
    }
}
