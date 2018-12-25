package com.natsuumeweb.niconico;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpResponse;
import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.natsuumeweb.api.NiconicoAPI;
import com.natsuumeweb.http.SimpleHttpClient;
import com.natsuumeweb.niconico.data.MyRepoData;
import com.natsuumeweb.niconico.data.NiconicoReport;

/**
 * ニコニコ動画のAPIを叩くクライアント
 * @author Natsuume
 *
 */
public class NiconicoClient {
	
	private LoginInfo loginInfo;
	private HttpCookie cookie;
	private Deque<NiconicoReport> reports = new ArrayDeque<>();
	private long latestReportId = 0;
	private Consumer<String> sendMessage;
	
	public NiconicoClient(Consumer<String> sendMessage) { 
		this.sendMessage = sendMessage;
	}
	
	public boolean login(String mail, String password) throws IOException, InterruptedException {
		loginInfo = new LoginInfo(mail, password);
		Map<String, String> headers = Map.of("Content-type", "application/x-www-form-urlencoded");
		Map<String, String> postMessages = Map.of("next_url", "", "mail", loginInfo.getMail(), "password", loginInfo.getPassword());
		SimpleHttpClient httpClient = new SimpleHttpClient(NiconicoAPI.LOGIN.getURIText(), Redirect.NEVER, headers, postMessages);
		HttpResponse<String> response = httpClient.sendPost();
		
		if(!httpClient.isPresentCookieHandler())
			return false;
		System.out.println(response.body());
		CookieStore store = ((CookieManager)httpClient.getCookieHandler()).getCookieStore();
		cookie = store.getCookies().stream()
				.filter(cookie -> cookie.getName().equals("user_session") &&
						!cookie.getValue().equals("deleted"))
				.findAny().orElse(null);
		loginInfo.setSession(cookie.toString());
		return loginInfo.isLogin();
	}

	public boolean retryLogin() throws IOException, InterruptedException {
		return login(loginInfo.getMail(), loginInfo.getPassword());
	}
	
	public String requestVideoViewHistory() throws IOException, InterruptedException {
		Map<String, String> headers = Map.of("Cookie", loginInfo.getCookie());
		
		SimpleHttpClient httpClient = new SimpleHttpClient(NiconicoAPI.VIDEO_VIEW_HISTORY.getURIText(), Redirect.ALWAYS, headers, Map.of());
		HttpResponse<String> response = httpClient.sendGet();
		String deserializedJson = new Gson().fromJson(response.body(), Map.class).toString();
		System.out.println(deserializedJson);

		return response.body();
	}
	
	public void getMyRepoData() throws IOException, InterruptedException {
		Map<String, String> headers = Map.of("Cookie", loginInfo.getCookie());
		SimpleHttpClient httpClient = new SimpleHttpClient(NiconicoAPI.MYREPO.getURIText(), Redirect.ALWAYS, headers, Map.of());
		HttpResponse<String> response = httpClient.sendGet();

		MyRepoData data = new Gson().fromJson(response.body(), MyRepoData.class);
		List<NiconicoReport> list = data.getReports().stream()
				.filter(report -> report.getLongId() > latestReportId)
				.sorted(Comparator.comparingLong(NiconicoReport::getLongId))
				.collect(Collectors.toList());
		latestReportId = list.get(list.size()-1).getLongId();
		String watchUri = NiconicoAPI.WATCH_PAGE.getURIText();
		list.stream()
				.filter(report -> report.getVideo() != null)
				.map(report -> report.getVideo().getWatchId())
				.distinct()
				.forEach(id -> sendMessage.accept(watchUri + id));
		
	}
}
