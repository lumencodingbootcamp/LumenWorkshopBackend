package com.lumen.LumenWorkshopBackend.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lumen.LumenWorkshopBackend.LumenMessage;

@Service
public class RedisMessageListener implements MessageListener {
	
	ObjectMapper mapper = new ObjectMapper();
	
	@Autowired
	WebSocketSessionService webSocketSessionService;
	
    @Override
    public void onMessage(Message message, byte[] pattern) {
        String channel = new String(message.getChannel(), StandardCharsets.UTF_8);
        String messageBody = new String(message.getBody(), StandardCharsets.UTF_8);

        System.out.println("Received message: " + messageBody + " on channel: " + channel);
        String cleanedMessage = cleanInvalidChars(messageBody);
        System.out.println(cleanedMessage);
        cleanedMessage = cleanedMessage.startsWith("{") ? cleanedMessage : cleanedMessage.substring(cleanedMessage.indexOf("{"));

        try {
			LumenMessage receivedMessage = mapper.readValue(cleanedMessage, LumenMessage.class);
			WebSocketSession session = webSocketSessionService.getUserSession(receivedMessage.getTo());
	        session.sendMessage(new TextMessage(cleanedMessage));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    private String cleanInvalidChars(String input) {
        return input.replaceAll("[^\\x20-\\x7E]", "");
    }
}

