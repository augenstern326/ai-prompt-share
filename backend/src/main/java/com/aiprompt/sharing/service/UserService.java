package com.aiprompt.sharing.service;

import com.aiprompt.sharing.dto.UserLoginDTO;
import com.aiprompt.sharing.dto.UserRegisterDTO;
import com.aiprompt.sharing.entity.User;
import com.aiprompt.sharing.vo.UserVO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 用户服务接口
 */
public interface UserService extends IService<User> {
    
    /**
     * 用户注册
     *
     * @param registerDTO 注册信息
     * @return 用户信息
     */
    UserVO register(UserRegisterDTO registerDTO);
    
    /**
     * 用户登录
     *
     * @param loginDTO 登录信息
     * @return 用户信息
     */
    UserVO login(UserLoginDTO loginDTO);
    
    /**
     * 获取当前登录用户信息
     *
     * @return 用户信息
     */
    UserVO getUserInfo(String userId);
    
    /**
     * 更新用户信息
     *
     * @param user 用户信息
     * @return 是否成功
     */
    boolean updateUserInfo(User user);
    
    /**
     * 修改用户状态
     *
     * @param userId 用户ID
     * @param status 状态
     * @return 是否成功
     */
    boolean updateUserStatus(String userId, Integer status);
}
