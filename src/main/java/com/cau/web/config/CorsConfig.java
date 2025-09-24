package com.cau.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")//允许所有请求
                //是否发送Cookie
                .allowCredentials(true)
                //放行哪些原始域
//                .allowedOriginPatterns("*")
        // 生产环境与本地允许的来源（仅列出前端页面所在域名；后端自身域名通常不必加入）
        .allowedOrigins(
            "https://libweb.csgone.cn",
            "http://libweb.csgone.cn",
            "http://localhost:9528"
        )
                .allowedMethods(new String[]{"GET", "POST", "PUT", "DELETE","OPTIONS"})
                .allowedHeaders("*")
                .exposedHeaders("*");
    }
}