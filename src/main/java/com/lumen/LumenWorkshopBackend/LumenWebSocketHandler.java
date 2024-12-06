package com.lumen.LumenWorkshopBackend;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lumen.LumenWorkshopBackend.config.ApplicationContextProvider;
import com.lumen.LumenWorkshopBackend.config.ConfigurationHelper;
import com.lumen.LumenWorkshopBackend.service.CassandraService;
import com.lumen.LumenWorkshopBackend.service.RedisUserChannelService;
import com.lumen.LumenWorkshopBackend.service.WebSocketSessionService;

public class LumenWebSocketHandler extends TextWebSocketHandler{
	
	
	ObjectMapper mapper = new ObjectMapper();
	
	LumenWebSocketHandler(CassandraService service){
		this.cassandraService = service;
	}
	
	private WebSocketSessionService getWebSocketSessionService() {
		ApplicationContext context = 
	            ApplicationContextProvider.getApplicationContext();
		return context.getBean(WebSocketSessionService.class);
	}
	
	private RedisUserChannelService getRedisUserChannelService() {
		ApplicationContext context = 
	            ApplicationContextProvider.getApplicationContext();
		return context.getBean(RedisUserChannelService.class);
	}
	
	private RedisTemplate<String,String> getRedisTemplate() {
		ApplicationContext context = 
	            ApplicationContextProvider.getApplicationContext();
		Map<String, RedisTemplate> beans = context.getBeansOfType(RedisTemplate.class);
        if (beans.size() > 1) {
            // Choose specific bean or throw custom exception
            return beans.get("redisTemplate");
        }
        return context.getBean(RedisTemplate.class);
	}

	CassandraService cassandraService;
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) {
		String connectionURL = session.getUri().getPath();
		String username = getUsername(connectionURL);
		cassandraService.insertUser(username);
		getRedisUserChannelService().registerUser(username, ConfigurationHelper.getServerName());
		getWebSocketSessionService().putUserSession(username, session);
	}
	
	private String getUsername(String connectionURL) {
		String[] parts = connectionURL.split("/");
		return parts[parts.length-1];
	}
	
	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws Exception {
		LumenMessage message = mapper.readValue(textMessage.getPayload(), LumenMessage.class);//It has to & content of the message
		try {
			String sender = getUsername(session.getUri().getPath());
			String to = message.getTo();
			
			LumenMessage toMessage = new LumenMessage();
			toMessage.setTo(to);
			toMessage.setFrom(sender);
			toMessage.setMessage(message.getMessage());
			toMessage.setMessageId(message.getMessageId());
			toMessage.setConversationKey(message.getConversationKey());
			
			cassandraService.addMessage(message.getConversationKey(), message.getMessage(), sender, to, message.getMessageId(), new Timestamp(System.currentTimeMillis()));
			
			//userSession.get(to).sendMessage(new TextMessage(mapper.writeValueAsString(toMessage)));
			sendMessage(to, mapper.writeValueAsString(toMessage));
			
			//Send Acknowledgement to sender
			LumenMessage ackMessage = message;
			ackMessage.setMessageType("ACK");
			session.sendMessage(new TextMessage(mapper.writeValueAsString(ackMessage)));
		}
		catch(Exception e) {
			e.printStackTrace();
			LumenMessage errMessage = message;
			errMessage.setMessageType("ERR");
			session.sendMessage(new TextMessage(mapper.writeValueAsString(errMessage)));
		}
	}
	
	@Override
	public void handleTransportError(WebSocketSession session,
			 Throwable exception) throws IOException {
		exception.printStackTrace();
	}

    // Send message to another user
    public void sendMessage(String receiver, String message) throws Exception {
        // Look up the channel for the receiver
        String receiverChannel = getRedisUserChannelService().getUserChannel(receiver);
        
        //If user is not registered for Lumen Chat
		if(receiverChannel==null) {
			throw new Exception("Person not available");
		}
		else {
            // Publish message to the channel for the receiver
            getRedisTemplate().convertAndSend(receiverChannel, message.getBytes(StandardCharsets.UTF_8));
        }
    }

}
