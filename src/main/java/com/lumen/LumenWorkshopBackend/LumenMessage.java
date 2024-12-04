package com.lumen.LumenWorkshopBackend;

public class LumenMessage {
	
	private String messageId;
	private String to;
	private String message;
	private String from;
	private String messageType;
	private String conversationKey;
	
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public String getConversationKey() {
		return conversationKey;
	}
	public void setConversationKey(String conversationKey) {
		this.conversationKey = conversationKey;
	}

}
