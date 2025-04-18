package com.aiprompt.sharing.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 提示词服务测试类
 */
@SpringBootTest
@Transactional
public class PromptServiceTest {

    @Autowired
    private PromptService promptService;

    @Test
    public void testCreatePrompt() {
        PromptCreateDTO createDTO = new PromptCreateDTO();
        createDTO.setTitle("测试提示词");
        createDTO.setContent("这是一个测试提示词的内容");
        createDTO.setImageUrl("test/image.jpg");
        List<Long> tagIds = new ArrayList<>();
        tagIds.add(1L);
        createDTO.setTagIds(tagIds);
        
        Long userId = 1L; // 假设用户ID为1
        
        Long promptId = promptService.createPrompt(createDTO, userId);
        
        assertNotNull(promptId);
        assertTrue(promptId > 0);
        
        Prompt prompt = promptService.getById(promptId);
        assertNotNull(prompt);
        assertEquals("测试提示词", prompt.getTitle());
        assertEquals("这是一个测试提示词的内容", prompt.getContent());
        assertEquals("test/image.jpg", prompt.getImageUrl());
        assertEquals(userId, prompt.getUserId());
    }

    @Test
    public void testGetPromptDetail() {
        // 先创建一个提示词
        PromptCreateDTO createDTO = new PromptCreateDTO();
        createDTO.setTitle("详情测试提示词");
        createDTO.setContent("这是用于测试获取详情的提示词内容");
        createDTO.setImageUrl("test/detail.jpg");
        
        Long userId = 1L; // 假设用户ID为1
        
        Long promptId = promptService.createPrompt(createDTO, userId);
        
        // 测试获取详情
        PromptVO promptVO = promptService.getPromptDetail(promptId, userId);
        
        assertNotNull(promptVO);
        assertEquals("详情测试提示词", promptVO.getTitle());
        assertEquals("这是用于测试获取详情的提示词内容", promptVO.getContent());
        assertEquals("test/detail.jpg", promptVO.getImageUrl());
    }

    @Test
    public void testUpdatePrompt() {
        // 先创建一个提示词
        PromptCreateDTO createDTO = new PromptCreateDTO();
        createDTO.setTitle("更新前提示词");
        createDTO.setContent("这是更新前的提示词内容");
        createDTO.setImageUrl("test/before.jpg");
        
        Long userId = 1L; // 假设用户ID为1
        
        Long promptId = promptService.createPrompt(createDTO, userId);
        
        // 测试更新
        PromptUpdateDTO updateDTO = new PromptUpdateDTO();
        updateDTO.setId(promptId);
        updateDTO.setTitle("更新后提示词");
        updateDTO.setContent("这是更新后的提示词内容");
        updateDTO.setImageUrl("test/after.jpg");
        
        promptService.updatePrompt(updateDTO, userId);
        
        // 验证更新结果
        Prompt prompt = promptService.getById(promptId);
        assertNotNull(prompt);
        assertEquals("更新后提示词", prompt.getTitle());
        assertEquals("这是更新后的提示词内容", prompt.getContent());
        assertEquals("test/after.jpg", prompt.getImageUrl());
    }

    @Test
    public void testDeletePrompt() {
        // 先创建一个提示词
        PromptCreateDTO createDTO = new PromptCreateDTO();
        createDTO.setTitle("待删除提示词");
        createDTO.setContent("这是待删除的提示词内容");
        
        Long userId = 1L; // 假设用户ID为1
        
        Long promptId = promptService.createPrompt(createDTO, userId);
        
        // 测试删除
        promptService.deletePrompt(promptId, userId, "user");
        
        // 验证删除结果（逻辑删除）
        Prompt prompt = promptService.getById(promptId);
        assertNotNull(prompt);
        assertEquals(0, prompt.getStatus()); // 状态为0表示已删除
    }
}
