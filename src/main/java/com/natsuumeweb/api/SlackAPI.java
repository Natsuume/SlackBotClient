package com.natsuumeweb.api;

public enum SlackAPI {
	CONNECT("https://slack.com/api/rtm.connect"),
	;
	
	private SlackAPI(String uri) {
		this.uri = uri;
	}
	
	private final String uri;
	
	public String getURIText() {
		return uri;
	}
}
