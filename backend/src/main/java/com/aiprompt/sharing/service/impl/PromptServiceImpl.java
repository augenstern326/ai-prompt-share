package com.aiprompt.sharing.service.impl;

import com.aiprompt.sharing.dto.PromptCreateDTO;
import com.aiprompt.sharing.dto.PromptUpdateDTO;
import com.aiprompt.sharing.entity.Favorite;
import com.aiprompt.sharing.entity.Prompt;
import com.aiprompt.sharing.entity.PromptTag;
import com.aiprompt.sharing.entity.Vote;
import com.aiprompt.sharing.mapper.FavoriteMapper;
import com.aiprompt.sharing.mapper.PromptMapper;
import com.aiprompt.sharing.mapper.PromptTagMapper;
import com.aiprompt.sharing.mapper.VoteMapper;
import com.aiprompt.sharing.service.PromptService;
import com.aiprompt.sharing.vo.PromptVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 提示词服务实现类
 */
@Service
public class PromptServiceImpl extends ServiceImpl<PromptMapper, Prompt> implements PromptService {

    @Autowired
    private PromptTagMapper promptTagMapper;
    
    @Autowired
    private VoteMapper voteMapper;
    
    @Autowired
    private FavoriteMapper favoriteMapper;

    @Autowired
    private PromptMapper promptMapper;

    @Override
    @Transactional
    public String createPrompt(PromptCreateDTO createDTO, String userId) {
        // 创建提示词
        Prompt prompt = new Prompt();
        prompt.setId(UUID.randomUUID().toString().replace("-",""));
        prompt.setTitle(createDTO.getTitle());
        prompt.setContent(createDTO.getContent());
        prompt.setImageUrl(createDTO.getImageUrl());
        prompt.setUserId(userId);
        prompt.setLikeCount(0);
        prompt.setDislikeCount(0);
        prompt.setFavoriteCount(0);
        prompt.setStatus(1); // 正常状态
        prompt.setCreateTime(LocalDateTime.now());
        prompt.setUpdateTime(LocalDateTime.now());
        
        // 保存提示词

        save(prompt);
        
        // 保存标签关联
        if (createDTO.getTagIds() != null && !createDTO.getTagIds().isEmpty()) {
            savePromptTags(prompt, createDTO.getTagIds());
        }
        
        return prompt.getId();
    }

    //保存提示词和标签关联
    private void savePromptTags(Prompt prompt, List<String> tagIds) {
        List<PromptTag> promptTags = tagIds.stream()
                .map(tagId -> {
                    PromptTag promptTag = new PromptTag();
                    promptTag.setPromptId(prompt.getId());
                    promptTag.setTagId(tagId);
                    return promptTag;
                })
                .collect(Collectors.toList());
        promptTagMapper.insertBatch(promptTags);
    }

    @Override
    public IPage<PromptVO> getPromptList(Integer current, Integer size, String keyword, List<String> tagIds, String sortBy) {
        Page<PromptVO> page = new Page<>(current, size);
        return page.setRecords(promptMapper.getPromptVOList(keyword, tagIds, sortBy));
    }

    @Override
    public PromptVO getPromptDetail(String promptId, String userId) {
        PromptVO promptVO = promptMapper.getPromptVOById(promptId);
        if (promptVO == null) {
            throw new RuntimeException("提示词不存在");
        }
        
        // 如果用户已登录，查询用户是否已点赞/点踩/收藏
        if (userId != null) {
            // 查询点赞/点踩状态
            LambdaQueryWrapper<Vote> voteQueryWrapper = new LambdaQueryWrapper<>();
            voteQueryWrapper.eq(Vote::getPromptId, promptId)
                    .eq(Vote::getUserId, userId);
            Vote vote = voteMapper.selectOne(voteQueryWrapper);
            if (vote != null) {
                if(vote.getType()==1){
                    promptVO.setLiked(true);
                    promptVO.setDisliked(false);
                }else{
                    promptVO.setLiked(false);
                    promptVO.setDisliked(true);
                }
            }
            
            // 查询收藏状态
            LambdaQueryWrapper<Favorite> favoriteQueryWrapper = new LambdaQueryWrapper<>();
            favoriteQueryWrapper.eq(Favorite::getPromptId, promptId)
                    .eq(Favorite::getUserId, userId);
            Favorite favorite = favoriteMapper.selectOne(favoriteQueryWrapper);
            if (favorite != null) {
                promptVO.setFavorited(true);
            }
        }
        
        return promptVO;
    }

    @Override
    @Transactional
    public void updatePrompt(PromptUpdateDTO updateDTO, String userId) {
        // 查询提示词
        Prompt prompt = getById(updateDTO.getId());
        if (prompt == null) {
            throw new RuntimeException("提示词不存在");
        }
        
        // 检查权限（只有提示词创建者或管理员可以修改）
        if (!prompt.getUserId().equals(userId) && !"admin".equals(updateDTO.getRole())) {
            throw new RuntimeException("无权修改此提示词");
        }
        
        // 更新提示词
        prompt.setTitle(updateDTO.getTitle());
        prompt.setContent(updateDTO.getContent());
        if (updateDTO.getImageUrl() != null) {
            prompt.setImageUrl(updateDTO.getImageUrl());
        }
        prompt.setUpdateTime(LocalDateTime.now());
        
        updateById(prompt);
        
        // 更新标签关联
        if (updateDTO.getTagIds() != null) {
            // 删除原有标签关联
            LambdaQueryWrapper<PromptTag> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(PromptTag::getPromptId, prompt.getId());
            promptTagMapper.delete(queryWrapper);
            
            // 添加新标签关联
            if (!updateDTO.getTagIds().isEmpty()) {
                savePromptTags(prompt, updateDTO.getTagIds());
            }
        }
    }

    @Override
    @Transactional
    public void deletePrompt(String promptId, String userId, String role) {
        // 查询提示词
        Prompt prompt = getById(promptId);
        if (prompt == null) {
            throw new RuntimeException("提示词不存在");
        }
        
        // 检查权限（只有提示词创建者或管理员可以删除）
        if (!prompt.getUserId().equals(userId) && !"admin".equals(role)) {
            throw new RuntimeException("无权删除此提示词");
        }
        
        // 逻辑删除提示词
        prompt.setStatus(0); // 设置为删除状态
        updateById(prompt);
    }

    @Override
    @Transactional
    public void likePrompt(String promptId, String userId) {
        // 查询提示词
        Prompt prompt = getById(promptId);
        if (prompt == null) {
            throw new RuntimeException("提示词不存在");
        }
        
        // 查询用户是否已点赞/点踩
        LambdaQueryWrapper<Vote> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Vote::getPromptId, promptId)
                .eq(Vote::getUserId, userId);
        Vote vote = voteMapper.selectOne(queryWrapper);
        
        if (vote != null) {
            // 已有点赞/点踩记录
            if (vote.getType() == 1) {
                // 已点赞，不做处理
                return;
            } else {
                // 已点踩，修改为点赞
                vote.setType(1);
                vote.setUpdateTime(LocalDateTime.now());
                voteMapper.updateById(vote);
                
                // 更新提示词点赞/点踩数
                prompt.setLikeCount(prompt.getLikeCount() + 1);
                prompt.setDislikeCount(prompt.getDislikeCount() - 1);
                updateById(prompt);
            }
        } else {
            // 无点赞/点踩记录，创建点赞记录
            vote = new Vote();
            vote.setPromptId(promptId);
            vote.setUserId(userId);
            vote.setType(1); // 点赞
            vote.setCreateTime(LocalDateTime.now());
            vote.setUpdateTime(LocalDateTime.now());
            voteMapper.insert(vote);
            
            // 更新提示词点赞数
            prompt.setLikeCount(prompt.getLikeCount() + 1);
            updateById(prompt);
        }
    }

    @Override
    @Transactional
    public void dislikePrompt(String promptId, String userId) {
        // 查询提示词
        Prompt prompt = getById(promptId);
        if (prompt == null) {
            throw new RuntimeException("提示词不存在");
        }
        
        // 查询用户是否已点赞/点踩
        LambdaQueryWrapper<Vote> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Vote::getPromptId, promptId)
                .eq(Vote::getUserId, userId);
        Vote vote = voteMapper.selectOne(queryWrapper);
        
        if (vote != null) {
            // 已有点赞/点踩记录
            if (vote.getType() == -1) {
                // 已点踩，不做处理
                return;
            } else {
                // 已点赞，修改为点踩
                vote.setType(-1);
                vote.setUpdateTime(LocalDateTime.now());
                voteMapper.updateById(vote);
                
                // 更新提示词点赞/点踩数
                prompt.setLikeCount(prompt.getLikeCount() - 1);
                prompt.setDislikeCount(prompt.getDislikeCount() + 1);
                updateById(prompt);
            }
        } else {
            // 无点赞/点踩记录，创建点踩记录
            vote = new Vote();
            vote.setPromptId(promptId);
            vote.setUserId(userId);
            vote.setType(-1); // 点踩
            vote.setCreateTime(LocalDateTime.now());
            vote.setUpdateTime(LocalDateTime.now());
            voteMapper.insert(vote);
            
            // 更新提示词点踩数
            prompt.setDislikeCount(prompt.getDislikeCount() + 1);
            updateById(prompt);
        }
    }

    @Override
    @Transactional
    public void cancelVote(String promptId, String userId) {
        // 查询提示词
        Prompt prompt = getById(promptId);
        if (prompt == null) {
            throw new RuntimeException("提示词不存在");
        }
        
        // 查询用户是否已点赞/点踩
        LambdaQueryWrapper<Vote> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Vote::getPromptId, promptId)
                .eq(Vote::getUserId, userId);
        Vote vote = voteMapper.selectOne(queryWrapper);
        
        if (vote != null) {
            // 删除点赞/点踩记录
            voteMapper.deleteById(vote.getId());
            
            // 更新提示词点赞/点踩数
            if (vote.getType() == 1) {
                // 取消点赞
                prompt.setLikeCount(prompt.getLikeCount() - 1);
            } else {
                // 取消点踩
                prompt.setDislikeCount(prompt.getDislikeCount() - 1);
            }
            updateById(prompt);
        }
    }

    @Override
    @Transactional
    public void favoritePrompt(String promptId, String userId) {
        // 查询提示词
        Prompt prompt = getById(promptId);
        if (prompt == null) {
            throw new RuntimeException("提示词不存在");
        }
        
        // 查询用户是否已收藏
        LambdaQueryWrapper<Favorite> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Favorite::getPromptId, promptId)
                .eq(Favorite::getUserId, userId);
        Favorite favorite = favoriteMapper.selectOne(queryWrapper);
        
        if (favorite == null) {
            // 创建收藏记录
            favorite = new Favorite();
            favorite.setPromptId(promptId);
            favorite.setUserId(userId);
            favorite.setCreateTime(LocalDateTime.now());
            favoriteMapper.insert(favorite);
            
            // 更新提示词收藏数
            prompt.setFavoriteCount(prompt.getFavoriteCount() + 1);
            updateById(prompt);
        }
    }

    @Override
    @Transactional
    public void cancelFavorite(String promptId, String userId) {
        // 查询提示词
        Prompt prompt = getById(promptId);
        if (prompt == null) {
            throw new RuntimeException("提示词不存在");
        }
        
        // 查询用户是否已收藏
        LambdaQueryWrapper<Favorite> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Favorite::getPromptId, promptId)
                .eq(Favorite::getUserId, userId);
        Favorite favorite = favoriteMapper.selectOne(queryWrapper);
        
        if (favorite != null) {
            // 删除收藏记录
            favoriteMapper.deleteById(favorite.getId());
            
            // 更新提示词收藏数
            prompt.setFavoriteCount(prompt.getFavoriteCount() - 1);
            updateById(prompt);
        }
    }

    @Override
    public IPage<PromptVO> getFavoriteList(String userId, Integer current, Integer size) {
        Page<PromptVO> page = new Page<>(current, size);
        return page.setRecords(promptMapper.getFavoritePromptVOList(userId));
    }
    
    @Override
    public void restorePrompt(String promptId) {
        // 查询提示词
        Prompt prompt = getById(promptId);
        if (prompt == null) {
            throw new RuntimeException("提示词不存在");
        }
        
        // 恢复提示词（将状态设为正常）
        prompt.setStatus(1);
        prompt.setUpdateTime(LocalDateTime.now());
        updateById(prompt);
    }
}
