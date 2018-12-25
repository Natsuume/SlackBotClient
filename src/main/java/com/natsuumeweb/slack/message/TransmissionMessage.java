package com.natsuumeweb.slack.message;

public abstract class TransmissionMessage {
	private int id = -1;
	private String type;
	
	public TransmissionMessage() {
		
	}
	
	public TransmissionMessage(String type) {
		this.type = type;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return this.id;
	}
	
	public String getType() {
		return this.type;
	}
}
