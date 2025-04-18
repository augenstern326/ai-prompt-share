package com.aiprompt.sharing.test;

import com.aiprompt.sharing.entity.User;
import com.aiprompt.sharing.service.UserService;
import com.aiprompt.sharing.dto.UserRegisterDTO;
import com.aiprompt.sharing.dto.UserLoginDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 用户服务测试类
 */
@SpringBootTest
@Transactional
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void testRegister() {
        UserRegisterDTO registerDTO = new UserRegisterDTO();
        registerDTO.setUsername("testuser");
        registerDTO.setPassword("password123");
        registerDTO.setEmail("test@example.com");
        
        Long userId = userService.register(registerDTO);
        
        assertNotNull(userId);
        assertTrue(userId > 0);
        
        User user = userService.getById(userId);
        assertNotNull(user);
        assertEquals("testuser", user.getUsername());
        assertEquals("test@example.com", user.getEmail());
        assertNotEquals("password123", user.getPassword()); // 密码应该被加密
    }

    @Test
    public void testLogin() {
        // 先注册用户
        UserRegisterDTO registerDTO = new UserRegisterDTO();
        registerDTO.setUsername("logintest");
        registerDTO.setPassword("password123");
        registerDTO.setEmail("login@example.com");
        
        Long userId = userService.register(registerDTO);
        
        // 测试登录
        UserLoginDTO loginDTO = new UserLoginDTO();
        loginDTO.setUsername("logintest");
        loginDTO.setPassword("password123");
        
        String token = userService.login(loginDTO);
        
        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    public void testLoginWithWrongPassword() {
        // 先注册用户
        UserRegisterDTO registerDTO = new UserRegisterDTO();
        registerDTO.setUsername("wrongpwdtest");
        registerDTO.setPassword("password123");
        registerDTO.setEmail("wrongpwd@example.com");
        
        Long userId = userService.register(registerDTO);
        
        // 测试错误密码登录
        UserLoginDTO loginDTO = new UserLoginDTO();
        loginDTO.setUsername("wrongpwdtest");
        loginDTO.setPassword("wrongpassword");
        
        assertThrows(RuntimeException.class, () -> {
            userService.login(loginDTO);
        });
    }
}
