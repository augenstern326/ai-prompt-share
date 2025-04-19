package com.aiprompt.sharing.service;

import com.aiprompt.sharing.dto.PromptCreateDTO;
import com.aiprompt.sharing.dto.PromptUpdateDTO;
import com.aiprompt.sharing.entity.Prompt;
import com.aiprompt.sharing.vo.PromptVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 提示词服务接口
 */
public interface PromptService extends IService<Prompt> {

    /**
     * 创建提示词
     */
    String createPrompt(PromptCreateDTO createDTO, String userId);

    /**
     * 获取提示词列表
     */
    IPage<PromptVO> getPromptList(Integer current, Integer size, String keyword, List<String> tagIds, String sortBy);

    /**
     * 获取提示词详情
     */
    PromptVO getPromptDetail(String promptId, String userId);

    /**
     * 更新提示词
     */
    void updatePrompt(PromptUpdateDTO updateDTO, String userId);

    /**
     * 删除提示词
     */
    void deletePrompt(String promptId, String userId, String role);
    
    /**
     * 恢复已删除的提示词（管理员）
     */
    void restorePrompt(String promptId);

    /**
     * 点赞提示词
     */
    void likePrompt(String promptId, String userId);

    /**
     * 点踩提示词
     */
    void dislikePrompt(String promptId, String userId);

    /**
     * 取消点赞/点踩
     */
    void cancelVote(String promptId, String userId);

    /**
     * 收藏提示词
     */
    void favoritePrompt(String promptId, String userId);

    /**
     * 取消收藏
     */
    void cancelFavorite(String promptId, String userId);

    /**
     * 获取收藏列表
     */
    IPage<PromptVO> getFavoriteList(String userId, Integer current, Integer size);
}
