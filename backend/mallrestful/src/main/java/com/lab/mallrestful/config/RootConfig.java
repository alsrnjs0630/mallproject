package com.lab.mallrestful.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RootConfig {
    @Bean
    public ModelMapper getMapper() {
        // ModelMapper : 객체 간의 매핑을 쉽게 도와주는 라이브러리 (DTO와 엔티티 간의 변환이 주요 용도)
        ModelMapper modelMapper = new ModelMapper();

        // 필드 이름을 기준으로 객체 간 매핑이 가능하도록 활성화
        // ModelMapper는 기본적으로 getter/setter를 사용해 매핑을 시도. 하지만 이 설정을 통해 필드 이름 자체를 기준으로 매핑이 이루어질 수 있음.
//        modelMapper.getConfiguration().setFieldMatchingEnabled();
        modelMapper.getConfiguration().setFieldMatchingEnabled(true)
                // 접근 수준을 PRIVATE으로 설정하여, private 필드도 매핑할 수 있도록 허용
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                // 매핑 전략 설정: 느슨한 매핑 전략(필드 이름이 정확히 일치하지 않더라도 유사한 이름이 있는 경우 매핑 시도 -> 유연한 매핑)
                .setMatchingStrategy(MatchingStrategies.LOOSE);

        return modelMapper;
    }
}
