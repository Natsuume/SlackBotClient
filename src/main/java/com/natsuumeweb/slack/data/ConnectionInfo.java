package com.natsuumeweb.slack.data;

import com.google.gson.annotations.SerializedName;

public class ConnectionInfo {
	@SerializedName("ok")
	private boolean isSucceed;
	
	@SerializedName("url")
	private String uri;
	
	public boolean isSucceed() {
		return isSucceed;
	}
	
	public String getURI() {
		return uri;
	}
}
