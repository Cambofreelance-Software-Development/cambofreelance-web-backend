package com.cambofreelance.authenticationservice.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${cms.upload.dir:uploads/logos}")
    private String uploadDir;

    @Value("${cms.upload.base-url:/uploads/logos}")
    private String uploadBaseUrl;

    @Value("${cms.media.upload-dir:uploads/media}")
    private String mediaUploadDir;

    @Value("${cms.media.base-url:/uploads/media}")
    private String mediaBaseUrl;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
            .addResourceHandler(uploadBaseUrl + "/**")
            .addResourceLocations("file:" + uploadDir + "/");

        registry
            .addResourceHandler(mediaBaseUrl + "/**")
            .addResourceLocations("file:" + mediaUploadDir + "/");
    }
}
