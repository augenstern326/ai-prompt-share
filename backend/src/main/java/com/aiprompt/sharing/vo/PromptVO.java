package com.aiprompt.sharing.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 提示词详情VO
 */
@Data
public class PromptVO {
    
    /**
     * 提示词ID
     */
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
     * 创建用户名
     */
    private String username;
    
    /**
     * 用户头像
     */
    private String userAvatar;
    
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
     * 标签列表
     */
    private List<TagVO> tags;
    
    /**
     * 当前用户是否点赞
     */
    private Boolean liked;
    
    /**
     * 当前用户是否点踩
     */
    private Boolean disliked;
    
    /**
     * 当前用户是否收藏
     */
    private Boolean favorited;
    
    /**
     * 创建时间
     */
    private Date createTime;
}
