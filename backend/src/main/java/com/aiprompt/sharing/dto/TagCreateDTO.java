package com.aiprompt.sharing.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 标签创建DTO
 */
@Data
public class TagCreateDTO {
    
    /**
     * 标签名称
     */
    @NotBlank(message = "标签名称不能为空")
    private String name;
    
    /**
     * 标签类型：0-系统预设，1-用户创建
     */
    @NotNull(message = "标签类型不能为空")
    private Integer type;



}
