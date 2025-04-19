package com.aiprompt.sharing.controller;

import com.aiprompt.sharing.common.Result;
import com.aiprompt.sharing.dto.TagCreateDTO;
import com.aiprompt.sharing.service.TagService;
import com.aiprompt.sharing.util.JwtUtil;
import com.aiprompt.sharing.vo.TagVO;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 标签控制器
 */
@RestController
@RequestMapping("/tag")
public class TagController {

    @Autowired
    private TagService tagService;
    
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 创建标签
     */
    @PostMapping("/create")
    @RequiresAuthentication
    public Result<?> createTag(@RequestBody @Validated TagCreateDTO createDTO, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            String userId = jwtUtil.getUserId(token);
            String role = jwtUtil.getRole(token);
            
            String tagId = tagService.createTag(createDTO, userId);
            return Result.success(tagId);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取标签列表
     */
    @GetMapping("/list")
    public Result<?> getTagList() {
        try {
            List<TagVO> tagList = tagService.getTagList();
            return Result.success(tagList);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
