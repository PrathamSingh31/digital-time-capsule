package com.capsule.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve uploaded images from /images/** URL
        Path imageDir = Paths.get("uploads/images");
        String imagePath = imageDir.toFile().getAbsolutePath();
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + imagePath + "/");

        // Serve uploaded videos from /videos/** URL
        Path videoDir = Paths.get("uploads/videos");
        String videoPath = videoDir.toFile().getAbsolutePath();
        registry.addResourceHandler("/videos/**")
                .addResourceLocations("file:" + videoPath + "/");
    }
}
