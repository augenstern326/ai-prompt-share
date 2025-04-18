package com.aiprompt.sharing.vo;

import lombok.Data;

/**
 * 用户信息VO
 */
@Data
public class UserVO {
    
    /**
     * 用户ID
     */
    private String id;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 邮箱
     */
    private String email;
    
    /**
     * 头像URL
     */
    private String avatar;
    
    /**
     * 角色：user-普通用户，admin-管理员
     */
    private String role;
    
    /**
     * token
     */
    private String token;
}
