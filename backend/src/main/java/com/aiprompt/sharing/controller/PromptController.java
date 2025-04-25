package com.aiprompt.sharing.controller;

import com.aiprompt.sharing.common.Result;
import com.aiprompt.sharing.dto.PromptCreateDTO;
import com.aiprompt.sharing.dto.PromptUpdateDTO;
import com.aiprompt.sharing.mapper.PromptTagMapper;
import com.aiprompt.sharing.service.PromptService;
import com.aiprompt.sharing.service.PromptTagService;
import com.aiprompt.sharing.util.JwtUtil;
import com.aiprompt.sharing.vo.PromptVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 提示词控制器
 */
@RestController
@RequestMapping("/prompt")
public class PromptController {

    @Autowired
    private PromptService promptService;
    
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private PromptTagService promptTagService;
    @Autowired
    private PromptTagMapper promptTagMapper;

    /**
     * 创建提示词
     */
    @PostMapping("/create")
    @RequiresAuthentication
    public Result<?> createPrompt(@RequestBody @Validated PromptCreateDTO createDTO, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            String userId = jwtUtil.getUserId(token);
            
            String promptId = promptService.createPrompt(createDTO, userId);
            return Result.success(promptId);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取提示词列表
     */
    @GetMapping("/list")
    public Result<?> getPromptList(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) List<String> tagIds,
            @RequestParam(defaultValue = "score") String sortBy) {
        try {
            IPage<PromptVO> page = promptService.getPromptList(current, size, keyword, tagIds, sortBy);
            return Result.success(page);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取提示词详情
     */
    @GetMapping("/detail/{id}")
    public Result<?> getPromptDetail(@PathVariable("id") String id, HttpServletRequest request) {
        try {
            String userId = null;
            String token = request.getHeader("Authorization");
            if (token != null) {
                userId = jwtUtil.getUserId(token);
            }
            
            PromptVO promptVO = promptService.getPromptDetail(id, userId);
            return Result.success(promptVO);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 更新提示词
     */
    @PutMapping("/update")
    @RequiresAuthentication
    public Result<?> updatePrompt(@RequestBody @Validated PromptUpdateDTO updateDTO, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            String userId = jwtUtil.getUserId(token);
            String role = jwtUtil.getRole(token);
            
            updateDTO.setRole(role);
            promptService.updatePrompt(updateDTO, userId);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 删除提示词
     */
    @DeleteMapping("/delete/{id}")
    @RequiresAuthentication
    public Result<?> deletePrompt(@PathVariable("id") String id, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            String userId = jwtUtil.getUserId(token);
            String role = jwtUtil.getRole(token);
            
            promptService.deletePrompt(id, userId, role);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 点赞提示词
     */
    @PostMapping("/like/{id}")
    @RequiresAuthentication
    public Result<?> likePrompt(@PathVariable("id") String id, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            String userId = jwtUtil.getUserId(token);
            
            promptService.likePrompt(id, userId);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 点踩提示词
     */
    @PostMapping("/dislike/{id}")
    @RequiresAuthentication
    public Result<?> dislikePrompt(@PathVariable("id") String id, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            String userId = jwtUtil.getUserId(token);
            
            promptService.dislikePrompt(id, userId);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 取消点赞/点踩
     */
    @PostMapping("/cancel-vote/{id}")
    @RequiresAuthentication
    public Result<?> cancelVote(@PathVariable("id") String id, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            String userId = jwtUtil.getUserId(token);
            
            promptService.cancelVote(id, userId);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 收藏提示词
     */
    @PostMapping("/favorite/{id}")
    @RequiresAuthentication
    public Result<?> favoritePrompt(@PathVariable("id") String id, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            String userId = jwtUtil.getUserId(token);
            
            promptService.favoritePrompt(id, userId);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 取消收藏
     */
    @PostMapping("/cancel-favorite/{id}")
    @RequiresAuthentication
    public Result<?> cancelFavorite(@PathVariable("id") String id, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            String userId = jwtUtil.getUserId(token);
            
            promptService.cancelFavorite(id, userId);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取收藏列表
     */
    @GetMapping("/favorite/list")
    @RequiresAuthentication
    public Result<?> getFavoriteList(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            String userId = jwtUtil.getUserId(token);
            
            IPage<PromptVO> page = promptService.getFavoriteList(userId, current, size);
            return Result.success(page);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
