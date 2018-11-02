package com.natsuumeweb.slack.info;

import com.google.gson.annotations.SerializedName;

public class ConnectionInfo {
	@SerializedName("ok")
	private boolean isSucceed;
	
	@SerializedName("url")
	private String uri;
	
	private SlackTeam team;

	@SerializedName("self")
	private SlackUser botUser;
	
	public boolean isSucceed() {
		return isSucceed;
	}
	
	public String getURI() {
		return uri;
	}
	
	public SlackTeam getTeam() {
		return team;
	}
	
	public SlackUser getBotUser() {
		return botUser;
	}
}
