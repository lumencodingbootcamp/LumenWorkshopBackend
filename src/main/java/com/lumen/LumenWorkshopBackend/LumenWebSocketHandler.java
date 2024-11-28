package com.lumen.LumenWorkshopBackend;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

public class LumenWebSocketHandler extends TextWebSocketHandler{
	
	Map<String, WebSocketSession> userSession = new HashMap<String, WebSocketSession>();
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String path = session.getUri().getPath();
        String username = extractUsername(path);
        userSession.put(username, session);
    }
	
	private String extractUsername(String path) {
        String[] parts = path.split("/");
        return parts[parts.length - 1];
    }
	
	@Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        LumenMessage senderMessage = objectMapper.readValue(payload, LumenMessage.class);

        LumenMessage receiversMessage = new LumenMessage();
        String path = session.getUri().getPath();
        String sender = extractUsername(path);
        receiversMessage.setFrom(sender);
        receiversMessage.setMessage(senderMessage.getMessage());
        userSession.get(senderMessage.getTo()).sendMessage(new TextMessage(objectMapper.writeValueAsString(receiversMessage)));

    }

}
