package com.natsuumeweb.niconico.data;

public class NiconicoReport {
	private String id;
	private String topic;
	private VideoData video;
	public String getId() {
		return id;
	}
	public String getTopic() {
		return topic;
	}
	public VideoData getVideo() {
		return video;
	}
	public long getLongId() {
		return Long.parseLong(id.split("-")[0]);
	}
}
