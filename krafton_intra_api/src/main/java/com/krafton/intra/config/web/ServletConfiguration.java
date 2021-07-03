package com.krafton.intra.config.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;

@Configuration
@EnableWebMvc
public class ServletConfiguration implements WebMvcConfigurer {

    @Bean
    public MessageSourceAccessor messageSourceAccessor(ReloadableResourceBundleMessageSource resourceBundleMessageSource) {
        MessageSourceAccessor messageSourceAccessor = new MessageSourceAccessor(resourceBundleMessageSource);
        return messageSourceAccessor;
    }

    @Bean
    public ReloadableResourceBundleMessageSource resourceBundleMessageSource(@Value("${error.message.reload.term}") int reloadTerm) {
        ReloadableResourceBundleMessageSource resourceBundleMessageSource = new ReloadableResourceBundleMessageSource();
        resourceBundleMessageSource.setCacheSeconds(reloadTerm);
        resourceBundleMessageSource.setAlwaysUseMessageFormat(false); //인자가 없는 메시지도 MessageFormat 규칙 적용할지
        resourceBundleMessageSource.setUseCodeAsDefaultMessage(true); //없는 메시지일 경우 Exception 발생시키는 대신 메시지 코드를 기본 메시지로 사용할지
        resourceBundleMessageSource.setDefaultEncoding(StandardCharsets.UTF_8.displayName()); //기본 인코딩
        resourceBundleMessageSource.setBasenames("classpath:message/messages", "classpath:message/error-messages");
        return resourceBundleMessageSource;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*");
    }

}
