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
                // 生产环境域名
                .allowedOrigins("http://101.200.87.86:8087","https://libweb.csgone.cn","https://api.csgone.cn")//此处需要重新设置

                // 本地调试
//                .allowedOrigins("http://localhost:9528")
                .allowedMethods(new String[]{"GET", "POST", "PUT", "DELETE","OPTIONS"})
                .allowedHeaders("*")
                .exposedHeaders("*");
    }
}