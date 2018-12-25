package com.natsuumeweb.slack.message;

import com.google.gson.annotations.SerializedName;

public class ErrorMessage {
	@SerializedName("msg")
	private String message;
	
	private int code;
	
	private String source;

	public String getMessage() {
		return message;
	}

	public int getCode() {
		return code;
	}

	public String getSource() {
		return source;
	}
	
	public String toString() {
		return code + " : " + message; 
	}
}
