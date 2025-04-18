package com.aiprompt.sharing.config.shiro;

import com.aiprompt.sharing.config.shiro.filters.JwtFilter;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Shiro配置类
 */
@Configuration
public class ShiroConfig {

    /**
     * 配置Shiro过滤器
     */
    @Bean("shiroFilterFactoryBean")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        // 添加自定义过滤器
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("jwt", new JwtFilter());
        shiroFilterFactoryBean.setFilters(filterMap);

        // 设置拦截器
        Map<String, String> filterRuleMap = new LinkedHashMap<>();
        
        // 不需要认证的接口
        filterRuleMap.put("/user/login", "anon");
        filterRuleMap.put("/user/register", "anon");
        filterRuleMap.put("/prompt/list", "anon");
        filterRuleMap.put("/prompt/detail/**", "anon");
        filterRuleMap.put("/tag/list", "anon");
        
        // 需要认证的接口
        filterRuleMap.put("/user/info", "jwt");
        filterRuleMap.put("/user/logout", "jwt");
        filterRuleMap.put("/prompt/create", "jwt");
        filterRuleMap.put("/prompt/update", "jwt");
        filterRuleMap.put("/prompt/delete/**", "jwt");
        filterRuleMap.put("/prompt/like/**", "jwt");
        filterRuleMap.put("/prompt/dislike/**", "jwt");
        filterRuleMap.put("/prompt/cancel-vote/**", "jwt");
        filterRuleMap.put("/prompt/favorite/**", "jwt");
        filterRuleMap.put("/prompt/cancel-favorite/**", "jwt");
        filterRuleMap.put("/prompt/favorite/list", "jwt");
        filterRuleMap.put("/tag/create", "jwt");
        filterRuleMap.put("/report/create", "jwt");
        
        // 管理员接口
        filterRuleMap.put("/admin/**", "jwt");
        filterRuleMap.put("/report/list", "jwt");
        filterRuleMap.put("/report/handle/**", "jwt");
        
        // 其他所有接口都需要认证
        filterRuleMap.put("/**", "jwt");
        
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterRuleMap);
        
        return shiroFilterFactoryBean;
    }

    /**
     * 配置安全管理器
     */
    @Bean("securityManager")
    public DefaultWebSecurityManager securityManager(ShiroRealm shiroRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(shiroRealm);
        
        // 关闭Shiro自带的session
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);
        
        return securityManager;
    }

    /**
     * 添加注解支持
     */
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        // 强制使用cglib，防止重复代理和可能引起代理出错的问题
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }
}
