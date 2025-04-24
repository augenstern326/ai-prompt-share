package com.aiprompt.sharing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 标签实体类
 */
@Data
@TableName("tag")
public class Tag implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 标签ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private String id;
    
    /**
     * 标签名称
     */
    private String name;
    
    /**
     * 标签类型：0-系统预设，1-用户创建
     */
    private Integer type;

    /**
     * 创建人ID
     */
    private String userId;



    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 更新时间
     */
    private Date updateTime;
    
    /**
     * 是否删除：0-未删除，1-已删除
     */
    private Integer deleted;
}
