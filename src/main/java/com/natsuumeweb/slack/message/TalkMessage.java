package com.natsuumeweb.slack.message;

import com.natsuumeweb.slack.data.MessageType;

public class TalkMessage extends TransmissionMessage{
	
	public TalkMessage(String text, String channel) {
		super(MessageType.MESSAGE.getText());
		
		this.channel = channel;
		this.text = text;
	}

	private String channel;
	
	private String text;
}
