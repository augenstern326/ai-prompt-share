package com.aiprompt.sharing.service.impl;

import com.aiprompt.sharing.dto.TagCreateDTO;
import com.aiprompt.sharing.entity.Tag;
import com.aiprompt.sharing.mapper.TagMapper;
import com.aiprompt.sharing.service.TagService;
import com.aiprompt.sharing.vo.TagVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 标签服务实现类
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Override
    public String createTag(TagCreateDTO createDTO, String userId) {
        // 检查标签名是否已存在
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Tag::getName, createDTO.getName());
        Tag existTag = getOne(queryWrapper);
        if (existTag != null) {
            return existTag.getId(); // 如果标签已存在，直接返回已有标签ID
        }
        
        // 创建新标签
        Tag tag = new Tag();
        tag.setName(createDTO.getName());
        tag.setType(createDTO.getType()); // 0-系统预设，1-用户创建
        tag.setUserId(userId);
        tag.setCreateTime(new Date());
        
        save(tag);
        return tag.getId();
    }

    @Override
    public List<TagVO> getTagList() {
        // 查询所有标签
        List<Tag> tags = list();
        
        // 转换为VO
        List<TagVO> tagVOs = tags.stream()
                .map(tag -> {
                    TagVO tagVO = new TagVO();
                    BeanUtils.copyProperties(tag, tagVO);
                    return tagVO;
                })
                .collect(Collectors.toList());
        
        return tagVOs;
    }
}
