package com.sggc.errors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebErrorConfiguration {

    @Value("${sggc.api.version}")
    private String currentApiVersion;

    @Bean
    public ErrorAttributes errorAttributes() {
        return new RequestErrorAttributes(currentApiVersion);
    }

}