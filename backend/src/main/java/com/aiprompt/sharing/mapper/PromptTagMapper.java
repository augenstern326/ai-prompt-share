package com.aiprompt.sharing.mapper;

import com.aiprompt.sharing.entity.PromptTag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PromptTagMapper extends BaseMapper<PromptTag> {

    //批量把提示词和对应的标签插入表中
    void insertBatch(List<PromptTag> promptTags);
}
