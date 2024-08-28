package com.lab.mallrestful.config;

import com.lab.mallrestful.security.filter.JWTCheckFilter;
import com.lab.mallrestful.security.handler.APILoginFailHandeler;
import com.lab.mallrestful.security.handler.APILoginSuccessHandler;
import com.lab.mallrestful.security.handler.CustomAccessDeniedHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration // Spring의 설정 클래스임을 나타냄. Spring 컨텍스트가 이 클래스를 스캔하여 필요한 Bean 생성
@Log4j2
@RequiredArgsConstructor
@EnableMethodSecurity
public class CustomSecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Spring Security의 필터 체인 설정.
        // HttpSecurity http => Spring Security의 주요 구성 요소. 다양한 보안 설정을 커스터마이징
        log.info("-------------securityConfig---------------");

        // CORS 설정
        http.cors(httpSecurityCorsConfigurer -> { // cors 설정 활성화
            // 미리 정의된 corsConfigurationSource() 사용하여 cors 정책 설정
            httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource());
        });

        // 세션 관리 설정: STATELESS
        http.sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.csrf(config -> config.disable());

        // formLogin 설정 추가 시 스프링 시큐리티는 POST 방식으로 username과 passwords라는 파라미터를 통해 로그인 처리
        http.formLogin(config -> {
            config.loginPage("/api/member/login");
            config.successHandler(new APILoginSuccessHandler());

            // 로그인 실패
            config.failureHandler(new APILoginFailHandeler());
        });

        http.addFilterBefore(new JWTCheckFilter(), UsernamePasswordAuthenticationFilter.class); // JWT 체크

        http.exceptionHandling(config -> {
            config.accessDeniedHandler(new CustomAccessDeniedHandler());
        });
        return http.build();
    }

    // CORS(Cross-Origin Resource Sharing) 정책 설정
    // => 애플리케이션이 다른 도메인, 포트, 프로토콜에서 온 요청을 어떻게 처리할지 정의
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        // CorsConfiguration : CORS 정책을 정의하는데 사용
        // 허용된 출처(origin), 메서드, 헤더, 자격 증명 등을 설정
        CorsConfiguration configuration = new CorsConfiguration();

        // setAllowedOriginPatterns : 허용된 출처(origin) 패턴 설정 : 현재 모든 출처 허용(= 모든 도메인에서 요청 허용)
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        // setAllowedMethods : 허용된 HTTP 메서드 지정. 지정된 요청만 허용
        configuration.setAllowedMethods(Arrays.asList("HEAD","GET", "POST", "PUT", "DELETE"));
        // setAllowedHeaders : 허용된 요청 헤더 지정. 클라이언트가 서버로 보낼 수 있는 헤더 정의
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        // setAllowCredentials : 자격 증명(쿠키, 인증 정보 등)을 포함한 요청을 허용할지 여부 설정
        // true => 클라이언트는 쿠키와 같은 자격 증명을 포함하여 요청을 보낼 수 있음.
        configuration.setAllowCredentials(true);

        // UrlBasedCorsConfigurationSource
        // CORS 설정을 URL 패턴에 따라 등록할 수 있는 클래스. URL 경로별로 서로 다른 CORS 정책을 적용할 수 있도록 지원
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 모든 경로에 동일한 CORS 설정을 적용
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
