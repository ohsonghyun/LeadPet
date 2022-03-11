package com.leadpet.www.config;

import com.leadpet.www.presentation.controller.annotation.config.UserTypeArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@lombok.RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 커스터마이즈 어노테이션 등록
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        WebMvcConfigurer.super.addArgumentResolvers(resolvers);

        // custom
        resolvers.add(new UserTypeArgumentResolver());
    }

    /**
     * CORS설정
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 모든 Origin에 오픈. 서버에서 필요에 따라 access 제어
        registry.addMapping("/v1/**")
                .allowedOrigins("*");
    }
}
