package com.aiprompt.sharing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 点赞/点踩实体类
 */
@Data
@TableName("vote")
public class Vote implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private String id;
    
    /**
     * 用户ID
     */
    private String userId;
    
    /**
     * 提示词ID
     */
    private String promptId;
    
    /**
     * 类型：1-点赞，-1-点踩
     */
    private Integer type;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
