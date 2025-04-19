package com.aiprompt.sharing.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 提示词创建DTO
 */
@Data
public class PromptCreateDTO {
    
    /**
     * 标题
     */
    @NotBlank(message = "标题不能为空")
    @Size(max = 100, message = "标题长度不能超过100个字符")
    private String title;
    
    /**
     * 提示词内容
     */
    @NotBlank(message = "提示词内容不能为空")
    private String content;
    
    /**
     * 效果图片URL
     */
    private String imageUrl;
    
    /**
     * 标签ID列表
     */
    @NotNull(message = "标签不能为空")
    private List<String> tagIds;
}
