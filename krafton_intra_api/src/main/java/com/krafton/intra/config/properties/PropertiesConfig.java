package com.krafton.intra.config.properties;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.ReloadingFileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.reloading.PeriodicReloadingTrigger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Configuration
public class PropertiesConfig {

    // 리로더블 공통코드 프로퍼티 파일
    @Bean
    public org.apache.commons.configuration2.ImmutableConfiguration commonCode(
            @Value("classpath:/properties/code.properties") Resource file) throws IOException, ConfigurationException {
        ReloadingFileBasedConfigurationBuilder builder =
                new ReloadingFileBasedConfigurationBuilder<>(
                        PropertiesConfiguration.class);
        builder.configure(new Parameters().fileBased().setFile(file.getFile()));
        PropertiesConfiguration props = new PropertiesConfiguration();
        PeriodicReloadingTrigger configReloadingTrigger = new PeriodicReloadingTrigger(
                builder.getReloadingController(), null, 3, TimeUnit.SECONDS);
        configReloadingTrigger.start();
        return builder.getConfiguration();
    }

}
