package com.aiprompt.sharing.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 提示词更新DTO
 */
@Data
public class PromptUpdateDTO {
    
    /**
     * 提示词ID
     */
    @NotNull(message = "提示词ID不能为空")
    private String id;
    
    /**
     * 标题
     */
    @NotBlank(message = "标题不能为空")
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

    /**
     * 更新人角色
     */
    private String role;
}
