package com.lumen.LumenWorkshopBackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisUserChannelService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    
    static {
    	System.out.println("REDIS==========================");
    }

    private static final String USER_CHANNEL_KEY = "user_channel_mapping";

    // Store the user-channel mapping
    public void registerUser(String username, String serverName) {
        redisTemplate.opsForHash().put(USER_CHANNEL_KEY, username, serverName);
    }

    // Retrieve the channel for a user
    public String getUserChannel(String username) {
        return (String) redisTemplate.opsForHash().get(USER_CHANNEL_KEY, username);
    }
}

