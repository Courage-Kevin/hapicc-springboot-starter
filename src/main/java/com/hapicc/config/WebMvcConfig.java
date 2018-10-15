package com.hapicc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.hapicc.controllers.interceptor.OneInterceptor;
import com.hapicc.controllers.interceptor.TwoInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 拦截器按照添加顺序进行拦截
        registry.addInterceptor(new OneInterceptor()).addPathPatterns("/*/**");
        registry.addInterceptor(new TwoInterceptor()).addPathPatterns("/one/**", "/two/**");
    }
}
