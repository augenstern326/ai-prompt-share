package com.aiprompt.sharing.service;

import com.aiprompt.sharing.dto.TagCreateDTO;
import com.aiprompt.sharing.entity.Tag;
import com.aiprompt.sharing.vo.TagVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 标签服务接口
 */
public interface TagService extends IService<Tag> {
    
    /**
     * 创建标签
     *
     * @param createDTO 创建信息
     * @return 标签ID
     */
    String createTag(TagCreateDTO createDTO, String userId);
    
    /**
     * 获取标签列表
     *
     * @return 标签列表
     */
    List<TagVO> getTagList();

}
