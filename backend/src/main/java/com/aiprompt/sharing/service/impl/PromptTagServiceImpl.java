package com.aiprompt.sharing.service.impl;

import com.aiprompt.sharing.entity.PromptTag;

import com.aiprompt.sharing.mapper.PromptTagMapper;
import com.aiprompt.sharing.service.PromptTagService;
import com.aiprompt.sharing.vo.TagVO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class PromptTagServiceImpl extends ServiceImpl<PromptTagMapper, PromptTag>  implements PromptTagService {

    @Autowired
    PromptTagMapper promptTagMapper;

    @Override
    public List<TagVO> getTagVOListByPromptId(String promptId) {
        return promptTagMapper.getTagVOListByPromptId(promptId);
    }
}
