package com.aiprompt.sharing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 提示词标签关联实体类
 */
@Data
@TableName("prompt_tag")
public class PromptTag implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private String id;
    
    /**
     * 提示词ID
     */
    private String promptId;
    
    /**
     * 标签ID
     */
    private String tagId;
    
    /**
     * 创建时间
     */
    private Date createTime;
}
