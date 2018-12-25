package com.natsuumeweb.slack.data;

import java.util.Arrays;

import com.natsuumeweb.slack.message.PingMessage;

public enum MessageType {
	HELLO("hello"),
	ERROR("error"),
	MESSAGE("message"),
	USER_TYPING("user_typing"),
	PING("ping", PingMessage.class),
	PONG("pong"),
	LOG("ok"),
	OTHER("return when nothing match typeText."),
	;
	
	
	private MessageType(String type, Class<?> c) {
		this.type = type;
		this.messageClass = c;
	}
	
	private MessageType(String type) {
		this(type, null);
	}
	
	private String type;
	private Class<?> messageClass;
	
	public String getText() {
		return type;
	}
	
	public Class<?> getMessageClass(){
		return messageClass;
	}
	
	public static MessageType toMessageType(String typeText) {
		return Arrays.stream(MessageType.values())
				.parallel()
				.filter(messageType -> messageType.type.equals(typeText))
				.findAny()
				.orElse(OTHER);
	}
}
