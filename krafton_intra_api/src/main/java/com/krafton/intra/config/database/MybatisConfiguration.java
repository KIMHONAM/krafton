package com.krafton.intra.config.database;

import org.apache.ibatis.session.Configuration;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class MybatisConfiguration{

    @Bean
    ConfigurationCustomizer mybatisConfigurationCustomizer() {  // mybatis 1.2.1 이상 
        return new ConfigurationCustomizer() {
            @Override
            public void customize(Configuration configuration) {
                configuration.setMapUnderscoreToCamelCase(true);    // db 컬럼명 자바 카멜케이스로 변환
                configuration.setCallSettersOnNulls(true);  // null 에 대한 setting 여부
            }
        };
    }

}
