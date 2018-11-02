package com.natsuumeweb.niconico;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.natsuumeweb.api.NiconicoAPI;
import com.natsuumeweb.http.SimpleHttpClient;

/**
 * ニコニコ動画のAPIを叩くクライアント
 * @author Natsuume
 *
 */
public class NiconicoClient {
	
	private LoginInfo loginInfo;
	private HttpCookie cookie;
	
	/**
	 * ニコニコ動画へログインし、ログイン時のuser sessionを保存する
	 * @param mail
	 * @param password
	 * @return ログインに成功したかどうか
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public boolean login(String mail, String password) throws IOException, InterruptedException {
		loginInfo = new LoginInfo(mail, password);
		Map<String, String> headers = Map.of("Content-type", "application/x-www-form-urlencoded");
		Map<String, String> postMessages = Map.of("next_url", "", "mail", loginInfo.getMail(), "password", loginInfo.getPassword());
		SimpleHttpClient httpClient = new SimpleHttpClient(NiconicoAPI.LOGIN.getURIText(), Redirect.NEVER, headers, postMessages);
		HttpResponse<String> response = httpClient.sendPost();
		
		if(!httpClient.isPresentCookieHandler())
			return false;

		CookieStore store = ((CookieManager)httpClient.getCookieHandler()).getCookieStore();
		cookie = store.getCookies().stream().filter(cookie -> cookie.getName().equals("user_session") && !cookie.getValue().equals("deleted")).findAny().orElse(null);
		loginInfo.setSession(cookie.toString());
		return loginInfo.isLogin();
	}
	
	/**
	 * 以前使用したログイン情報で再度ログインする
	 * @return ログインに成功したかどうか
	 * @throws IOException
	 * @throws InterruptedException
	 */
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
}
