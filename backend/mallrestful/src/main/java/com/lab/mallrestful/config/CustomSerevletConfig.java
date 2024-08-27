package com.lab.mallrestful.config;

import com.lab.mallrestful.controller.formatter.LocalDateFormatter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CustomSerevletConfig implements WebMvcConfigurer {

    @Override
    // FormatterRegistry를 사용하여 커스텀 포맷터를 등록
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new LocalDateFormatter());
    }

    /*@Override
    *//*
    CORS(Cross-Origin Resource Sharing) 설정
    웹 애플리케이션이 다른 도메인, 프로토콜, 포트에서 요청을 수락할 수 있도록 제어
    서로 다른 출처 간의 요청을 허용하거나 제한하는데 사용
    *//*
    // CorsRegistry : CORS 설정을 추가하고 관리하는 객체
    public void addCorsMappings(CorsRegistry registry) {
        // addMapping("/**") : 모든 URL 경로에 대해 CORS 설정 적용
        registry.addMapping("/**")
                // 모든 출처(도메인)에서의 요청을 허용
                .allowedOrigins("*")
                // 허용 되는 HTTP 메서드를 설정
                .allowedMethods("HEAD", "GET", "POST", "PUT", "DELETE", "OPTIONS")
                // CORS 응답의 캐시 유효 기간 (300초)
                .maxAge(300)
                // 허용되는 요청 헤더 설정
                .allowedHeaders("Authorization", "Cache-Control", "Content-Type");
    }*/
}
