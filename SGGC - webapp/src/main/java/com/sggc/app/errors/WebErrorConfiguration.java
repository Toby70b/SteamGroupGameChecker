package com.sggc.app.errors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebErrorConfiguration {

    @Value("${requests.api.version}")
    private String currentApiVersion;

    @Bean
    public ErrorAttributes errorAttributes() {
        return new RequestErrorAttributes(currentApiVersion);
    }

}