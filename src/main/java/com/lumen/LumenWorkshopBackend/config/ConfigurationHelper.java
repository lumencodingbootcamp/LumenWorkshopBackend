package com.lumen.LumenWorkshopBackend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class ConfigurationHelper {
    @Value("${server.name}")
    private String serverName;

    private static ConfigurationHelper instance;

    @PostConstruct
    public void init() {
        instance = this;
    }

    public static String getServerName() {
        return instance.serverName;
    }
}
