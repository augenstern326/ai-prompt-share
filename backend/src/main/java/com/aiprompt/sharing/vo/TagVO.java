package com.aiprompt.sharing.vo;

import lombok.Data;

/**
 * 标签VO
 */
@Data
public class TagVO {
    
    /**
     * 标签ID
     */
    private String id;
    
    /**
     * 标签名称
     */
    private String name;
    
    /**
     * 标签类型：0-系统预设，1-用户创建
     */
    private Integer type;
}
