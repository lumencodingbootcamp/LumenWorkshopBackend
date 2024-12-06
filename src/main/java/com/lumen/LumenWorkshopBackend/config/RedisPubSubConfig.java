package com.lumen.LumenWorkshopBackend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import com.lumen.LumenWorkshopBackend.service.RedisMessageListener;

@Configuration
public class RedisPubSubConfig {

    @Autowired
    private RedisMessageListener redisMessageListener;

    @Value("${server.name}") // Dynamically get server name (e.g., server1, server2)
    private String serverName;

    @Bean
    public RedisMessageListenerContainer messageListenerContainer(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        // Subscribe to a channel named after the server (e.g., server1)
        container.addMessageListener(redisMessageListener, new ChannelTopic(serverName));

        return container;
    }
}

