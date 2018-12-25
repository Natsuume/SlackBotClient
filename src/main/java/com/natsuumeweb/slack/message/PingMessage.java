package com.natsuumeweb.slack.message;

public class PingMessage extends TransmissionMessage{
	public PingMessage(String type) {
		super(type);
	}
	
	public PingMessage(int id, String type) {
		super(type);
		setId(id);
	}
}
