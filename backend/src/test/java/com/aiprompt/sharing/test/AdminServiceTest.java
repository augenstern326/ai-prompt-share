package com.aiprompt.sharing.test;

import com.aiprompt.sharing.vo.UserVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 管理员服务测试类
 */
@SpringBootTest
@Transactional
public class AdminServiceTest {

    @Autowired
    private AdminService adminService;

    @Test
    public void testGetUserList() {
        // 测试获取用户列表
        IPage<UserVO> userPage = adminService.getUserList(1, 10, null, null, null);
        
        assertNotNull(userPage);
        assertNotNull(userPage.getRecords());
    }

    @Test
    public void testUpdateUserStatus() {
        // 假设用户ID为1
        Long userId = 1L;
        Integer status = 0; // 禁用状态
        
        // 测试更新用户状态
        adminService.updateUserStatus(userId, status);
        
        // 验证更新结果
        IPage<UserVO> userPage = adminService.getUserList(1, 10, null, null, status);
        boolean found = false;
        for (UserVO user : userPage.getRecords()) {
            if (user.getId().equals(userId)) {
                assertEquals(status, user.getStatus());
                found = true;
                break;
            }
        }
        assertTrue(found, "未找到更新后的用户");
    }

    @Test
    public void testUpdateUserRole() {
        // 假设用户ID为1
        Long userId = 1L;
        String role = "admin"; // 管理员角色
        
        // 测试更新用户角色
        adminService.updateUserRole(userId, role);
        
        // 验证更新结果
        IPage<UserVO> userPage = adminService.getUserList(1, 10, null, role, null);
        boolean found = false;
        for (UserVO user : userPage.getRecords()) {
            if (user.getId().equals(userId)) {
                assertEquals(role, user.getRole());
                found = true;
                break;
            }
        }
        assertTrue(found, "未找到更新后的用户");
    }
}
