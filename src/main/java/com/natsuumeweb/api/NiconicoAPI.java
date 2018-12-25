package com.natsuumeweb.api;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public enum NiconicoAPI {
	LOGIN("https://secure.nicovideo.jp/secure/login?site=niconico"),
	WATCH_PAGE("https://www.nicovideo.jp/watch/"),
	VIDEO_VIEW_HISTORY("http://www.nicovideo.jp/api/videoviewhistory/list"),
	MYLIST_LIST("http://www.nicovideo.jp/api/mylistgroup/list"),
	MYREPO("http://www.nicovideo.jp/api/nicorepo/timeline/my/all?client_app=pc_myrepo"),
	;
	
	
	private NiconicoAPI(String uri) {
		this.uri = uri;
	}
	
	private final String uri;
	public String getURIText() {
		return uri;
	}
}
