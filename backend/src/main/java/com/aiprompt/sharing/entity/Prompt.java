package com.aiprompt.sharing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 提示词实体类
 */
@Data
@TableName("prompt")
public class Prompt implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 提示词ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private String id;
    
    /**
     * 标题
     */
    private String title;
    
    /**
     * 提示词内容
     */
    private String content;
    
    /**
     * 效果图片URL
     */
    private String imageUrl;
    
    /**
     * 创建用户ID
     */
    private String userId;
    
    /**
     * 点赞数
     */
    private Integer likeCount;
    
    /**
     * 点踩数
     */
    private Integer dislikeCount;
    
    /**
     * 收藏数
     */
    private Integer favoriteCount;
    
    /**
     * 浏览数
     */
    private Integer viewCount;
    
    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    /**
     * 是否删除：0-未删除，1-已删除
     */
    @TableLogic
    private Integer deleted;
}
