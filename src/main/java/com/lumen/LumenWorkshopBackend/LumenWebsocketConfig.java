package com.lumen.LumenWorkshopBackend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.lumen.LumenWorkshopBackend.service.CassandraService;

@Configuration
@EnableWebSocket
public class LumenWebsocketConfig implements WebSocketConfigurer{
	
	@Autowired
	CassandraService service;

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(new LumenWebSocketHandler(service), "/chat/*")
        	.setAllowedOrigins("*");
	}

}
