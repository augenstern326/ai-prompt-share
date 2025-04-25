package com.aiprompt.sharing.service;

import com.aiprompt.sharing.entity.PromptTag;
import com.aiprompt.sharing.vo.TagVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface PromptTagService extends IService<PromptTag> {
    List<TagVO> getTagVOListByPromptId(String promptId);
}
