package com.aiprompt.sharing.controller;

import com.aiprompt.sharing.common.Result;
import com.aiprompt.sharing.dto.UserLoginDTO;
import com.aiprompt.sharing.dto.UserRegisterDTO;
import com.aiprompt.sharing.service.UserService;
import com.aiprompt.sharing.util.JwtUtil;
import com.aiprompt.sharing.vo.UserVO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.aiprompt.sharing.entity.User;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result register(@RequestBody @Validated UserRegisterDTO registerDTO) {
        try {
            UserVO userVO = userService.register(registerDTO);
            return Result.success(userVO);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result login(@RequestBody @Validated UserLoginDTO loginDTO) {
        try {
            UserVO userVO = userService.login(loginDTO);
            return Result.success(userVO);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取用户信息
     */
    @GetMapping("/info")
    @RequiresAuthentication
    public Result getUserInfo(HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            String  userId = jwtUtil.getUserId(token);
            UserVO userVO = userService.getUserInfo(userId);
            return Result.success(userVO);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/info")
    @RequiresAuthentication
    public Result updateUserInfo(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            String  userId = jwtUtil.getUserId(token);
            
            String email = params.get("email") != null ? params.get("email").toString() : null;
            String newPassword = params.get("newPassword") != null ? params.get("newPassword").toString() : null;
            String avatar = params.get("avatar") != null ? params.get("avatar").toString() : null;

            User user = new User();
            user.setId(userId);
            user.setEmail(email);
            user.setPassword(newPassword);
            user.setAvatar(avatar);
            userService.updateUserInfo(user);
            
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 用户登出
     */
    @PostMapping("/logout")
    @RequiresAuthentication
    public Result logout() {
        SecurityUtils.getSubject().logout();
        return Result.success("登出成功");
    }
}
