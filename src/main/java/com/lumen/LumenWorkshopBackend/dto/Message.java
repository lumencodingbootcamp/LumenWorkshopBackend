package com.lumen.LumenWorkshopBackend.dto;

import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Column;

import java.util.UUID;
import java.sql.Timestamp;

@Table(keyspace = "lumenkeyspace", value = "messages")
public class Message {

    @PrimaryKey
    private String messageId;

    @Column("conversationkey")
    private String conversationKey;

    @Column("sender")
    private String sender;

    @Column("receiver")
    private String receiver;

    @Column("message")
    private String message;

    @Column("timestamp")
    private Timestamp timestamp;

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

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
}

