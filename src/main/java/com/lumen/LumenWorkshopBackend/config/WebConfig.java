package com.lumen.LumenWorkshopBackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // Allow CORS for any endpoint starting with "/api"
                .allowedOrigins("*")  // Allow your React frontend
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // Include OPTIONS
                .allowedHeaders("*");
    }
}