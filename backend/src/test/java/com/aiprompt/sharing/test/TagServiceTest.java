package com.aiprompt.sharing.test;

import com.aiprompt.sharing.dto.TagCreateDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 标签服务测试类
 */
@SpringBootTest
@Transactional
public class TagServiceTest {

    @Autowired
    private TagService tagService;

    @Test
    public void testCreateTag() {
        TagCreateDTO createDTO = new TagCreateDTO();
        createDTO.setName("测试标签");
        createDTO.setType(1); // 用户创建
        
        Long userId = 1L; // 假设用户ID为1
        String role = "user"; // 普通用户角色
        
        Long tagId = tagService.createTag(createDTO, userId, role);
        
        assertNotNull(tagId);
        assertTrue(tagId > 0);
        
        // 验证创建结果
        List<TagVO> tagList = tagService.getTagList();
        boolean found = false;
        for (TagVO tag : tagList) {
            if (tag.getId().equals(tagId)) {
                assertEquals("测试标签", tag.getName());
                assertEquals(1, tag.getType());
                assertEquals(userId, tag.getUserId());
                found = true;
                break;
            }
        }
        assertTrue(found, "未找到创建的标签");
    }

    @Test
    public void testGetTagList() {
        // 先创建几个标签
        TagCreateDTO createDTO1 = new TagCreateDTO();
        createDTO1.setName("标签1");
        createDTO1.setType(1);
        
        TagCreateDTO createDTO2 = new TagCreateDTO();
        createDTO2.setName("标签2");
        createDTO2.setType(1);
        
        Long userId = 1L;
        String role = "user";
        
        tagService.createTag(createDTO1, userId, role);
        tagService.createTag(createDTO2, userId, role);
        
        // 测试获取标签列表
        List<TagVO> tagList = tagService.getTagList();
        
        assertNotNull(tagList);
        assertTrue(tagList.size() >= 2);
        
        // 验证是否包含创建的标签
        boolean found1 = false;
        boolean found2 = false;
        for (TagVO tag : tagList) {
            if ("标签1".equals(tag.getName())) {
                found1 = true;
            } else if ("标签2".equals(tag.getName())) {
                found2 = true;
            }
        }
        assertTrue(found1, "未找到标签1");
        assertTrue(found2, "未找到标签2");
    }
}
