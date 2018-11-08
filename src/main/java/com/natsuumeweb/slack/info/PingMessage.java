package com.natsuumeweb.slack.info;

import com.google.gson.annotations.SerializedName;

public class PingMessage {
	@SerializedName("id")
	private String message;
	private String type;
	
	public PingMessage() {
		
	}
	
	public PingMessage(String message, String type) {
		this.message = message;
		this.type = type;
	}
	
	public String getMessage() {
		return message;
	}
	
	public String getType() {
		return type;
	}
}
