package com.aiprompt.sharing.service.impl;

import com.aiprompt.sharing.dto.UserLoginDTO;
import com.aiprompt.sharing.dto.UserRegisterDTO;
import com.aiprompt.sharing.entity.User;
import com.aiprompt.sharing.mapper.UserMapper;
import com.aiprompt.sharing.service.UserService;
import com.aiprompt.sharing.util.JwtUtil;
import com.aiprompt.sharing.vo.UserVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private JwtUtil jwtUtil;

    private static final String SALT = "AiPromptSharing";
    private static final int HASH_ITERATIONS = 1024;

    @Override
    public UserVO register(UserRegisterDTO registerDTO) {
        // 检查用户名是否已存在
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, registerDTO.getUsername());
        User existUser = getOne(queryWrapper);
        if (existUser != null) {
            throw new RuntimeException("用户名已存在");
        }

        // 检查邮箱是否已存在
        queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, registerDTO.getEmail());
        existUser = getOne(queryWrapper);
        if (existUser != null) {
            throw new RuntimeException("邮箱已被注册");
        }

        // 创建新用户
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setEmail(registerDTO.getEmail());
        // 密码加密
        String encryptedPassword = new Sha256Hash(registerDTO.getPassword(), SALT, HASH_ITERATIONS).toHex();
        user.setPassword(encryptedPassword);
        user.setRole("user"); // 默认为普通用户
        user.setStatus(1); // 默认状态为正常
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        // 保存用户
        save(user);

        // 生成token
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());

        // 返回用户信息
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        userVO.setToken(token);

        return userVO;
    }

    @Override
    public UserVO login(UserLoginDTO loginDTO) {
        // 查询用户
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, loginDTO.getUsername());
        User user = getOne(queryWrapper);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 检查用户状态
        if (user.getStatus() == 0) {
            throw new RuntimeException("账号已被禁用");
        }

        // 验证密码
        String encryptedPassword = new Sha256Hash(loginDTO.getPassword(), SALT, HASH_ITERATIONS).toHex();
        if (!user.getPassword().equals(encryptedPassword)) {
            throw new RuntimeException("密码错误");
        }

        // 生成token
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());

        // 返回用户信息
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        userVO.setToken(token);

        return userVO;
    }

    @Override
    public UserVO getUserInfo(String  userId) {
        User user = getById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    @Override
    public boolean updateUserInfo(User user) {
        return false;
    }

    @Override
    public boolean updateUserStatus(String userId, Integer status) {
        return false;
    }
}
