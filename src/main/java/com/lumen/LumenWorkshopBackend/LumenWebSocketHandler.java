package com.lumen.LumenWorkshopBackend;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lumen.LumenWorkshopBackend.service.CassandraService;

public class LumenWebSocketHandler extends TextWebSocketHandler{
	
	Map<String, WebSocketSession> userSession = new HashMap<String, WebSocketSession>();
	ObjectMapper mapper = new ObjectMapper();
	
	LumenWebSocketHandler(CassandraService service){
		this.cassandraService = service;
	}

	CassandraService cassandraService;
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) {
		String connectionURL = session.getUri().getPath();
		String username = getUsername(connectionURL);
		cassandraService.insertUser(username);
		userSession.put(username, session);
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
			
			//If user is not registered for Lumen Chat
			if(!userSession.containsKey(to)) {
				throw new Exception("Person not available");
			}
			LumenMessage toMessage = new LumenMessage();
			toMessage.setFrom(sender);
			toMessage.setMessage(message.getMessage());
			toMessage.setMessageId(message.getMessageId());
			toMessage.setConversationKey(message.getConversationKey());
			
			cassandraService.addMessage(message.getConversationKey(), message.getMessage(), sender, to, message.getMessageId(), new Timestamp(System.currentTimeMillis()));
			
			userSession.get(to).sendMessage(new TextMessage(mapper.writeValueAsString(toMessage)));
			
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

}
