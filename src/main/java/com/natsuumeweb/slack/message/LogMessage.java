package com.natsuumeweb.slack.message;

import com.google.gson.annotations.SerializedName;

public class LogMessage {
	@SerializedName("ok")
	private boolean isOk;
	
	@SerializedName("reply_to")
	private int replyTo;
	
	@SerializedName("ts")
	private String timeStamp;
	
	private String text;
	
	private ErrorMessage error;

	public boolean isOk() {
		return isOk;
	}

	public int getReplyTo() {
		return replyTo;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public String getText() {
		return text;
	}

	public ErrorMessage getError() {
		return error;
	}
}
