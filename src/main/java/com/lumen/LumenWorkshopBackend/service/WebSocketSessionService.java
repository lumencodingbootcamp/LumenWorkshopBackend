package com.lumen.LumenWorkshopBackend.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Component
public class WebSocketSessionService {
	
	Map<String, WebSocketSession> userSession = new HashMap<String, WebSocketSession>();
	
	public WebSocketSession getUserSession(String user){
		return userSession.get(user);
	}
	
	public void putUserSession(String user, WebSocketSession session) {
		userSession.put(user, session);
	}

}
