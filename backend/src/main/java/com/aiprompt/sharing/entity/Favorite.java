package com.aiprompt.sharing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 收藏实体类
 */
@Data
@TableName("favorite")
public class Favorite implements Serializable {
    
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
     * 创建时间
     */
    private LocalDateTime createTime;
}
