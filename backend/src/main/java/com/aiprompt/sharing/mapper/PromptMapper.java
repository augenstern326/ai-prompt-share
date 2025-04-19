package com.aiprompt.sharing.mapper;

import com.aiprompt.sharing.entity.Prompt;
import com.aiprompt.sharing.vo.PromptVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PromptMapper extends BaseMapper<Prompt> {
    List<PromptVO> getPromptVOList(String keyword, List<String> tagIds, String sortBy);

    PromptVO getPromptVOById(String promptId);

    List<PromptVO> getFavoritePromptVOList(String userId);
}
