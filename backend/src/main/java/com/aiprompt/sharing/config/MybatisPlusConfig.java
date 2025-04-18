package com.aiprompt.sharing.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.aiprompt.sharing.mapper")
public class MybatisPlusConfig {
    
}
