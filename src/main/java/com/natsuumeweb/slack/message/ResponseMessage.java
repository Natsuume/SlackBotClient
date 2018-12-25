package com.natsuumeweb.slack.message;

import com.google.gson.annotations.SerializedName;

public class ResponseMessage{
	private String user;
	
	private String text;
	
	@SerializedName("client_msg_id")
	private String clientMessageId;
	
	@SerializedName("team")
	private String teamId;
	
	@SerializedName("channel")
	private String channel;
	
	@SerializedName("event_ts")
	private String eventTimeStamp;
	
	@SerializedName("ts")
	private String timeStamp;

	public String getUser() {
		return user;
	}

	public String getText() {
		return text;
	}

	public String getClientMessageId() {
		return clientMessageId;
	}

	public String getTeamId() {
		return teamId;
	}

	public String getChannel() {
		return channel;
	}

	public String getEventTimeStamp() {
		return eventTimeStamp;
	}

	public String getTimeStamp() {
		return timeStamp;
	}
	
	public String toString() {
		return "[user:"+user + "] (channel:" + channel +") message:" + text;
	}
}
