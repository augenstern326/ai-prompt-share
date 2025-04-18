package com.aiprompt.sharing.config.shiro;

import com.aiprompt.sharing.util.JwtUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * 自定义Realm，处理用户认证和授权
 */
@Component
public class ShiroRealm extends AuthorizingRealm {

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 必须重写此方法，不然会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 授权(验证权限时调用)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String token = (String) principals.getPrimaryPrincipal();
        String role = jwtUtil.getRole(token);
        
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        
        // 添加角色
        Set<String> roleSet = new HashSet<>();
        roleSet.add(role);
        info.setRoles(roleSet);
        
        // 添加权限
        Set<String> permissionSet = new HashSet<>();
        if ("admin".equals(role)) {
            permissionSet.add("user:*");
            permissionSet.add("prompt:*");
            permissionSet.add("report:*");
            permissionSet.add("admin:*");
        } else {
            permissionSet.add("user:view");
            permissionSet.add("user:update");
            permissionSet.add("prompt:view");
            permissionSet.add("prompt:create");
            permissionSet.add("prompt:update");
            permissionSet.add("prompt:delete");
            permissionSet.add("favorite:*");
            permissionSet.add("vote:*");
            permissionSet.add("report:create");
        }
        info.setStringPermissions(permissionSet);
        
        return info;
    }

    /**
     * 认证(登录时调用)
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String token = (String) authenticationToken.getCredentials();
        
        // 验证token有效性
        if (token == null || !jwtUtil.verify(token)) {
            throw new AuthenticationException("token无效");
        }
        
        // 验证token是否过期
        if (jwtUtil.isExpired(token)) {
            throw new AuthenticationException("token已过期");
        }
        
        return new SimpleAuthenticationInfo(token, token, getName());
    }
}
