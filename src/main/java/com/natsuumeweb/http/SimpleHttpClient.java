package com.natsuumeweb.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * HttpClientを利用したリクエストの簡易クラス
 * @author Natsuume
 *
 */
public class SimpleHttpClient {
	private final String encode = "utf-8";
	
	private String uri;
	private Map<String, String> headers;
	private Map<String, String> messages;
	private HttpClient httpClient;
		
	public SimpleHttpClient(String uri) {
		this(uri, Redirect.ALWAYS);
	}
	
	public SimpleHttpClient(String uri, Redirect redirect) {
		this(uri, redirect, Map.of(), Map.of());
	}
	
	public SimpleHttpClient(String uri, Map<String, String> headers, Map<String, String> messages) {
		this(uri, Redirect.ALWAYS, headers, messages);
	}
	
	public SimpleHttpClient(String uri, Redirect redirect, Map<String, String> headers, Map<String, String> messages) {
		this.uri = uri;
		this.headers = headers;
		this.messages = messages;
		this.httpClient = HttpClient.newBuilder()
				.followRedirects(redirect)
				.cookieHandler(new CookieManager())
				.build();
	}
	
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}
	
	public void setMessages(Map<String, String> messages) {
		this.messages = messages;
	}
	
	public HttpResponse<String> sendPost() throws IOException, InterruptedException {
		Builder builder = HttpRequest.newBuilder(URI.create(uri));
		headers.entrySet().stream().forEach(entry -> builder.header(entry.getKey(), entry.getValue()));
		builder.POST(BodyPublishers.ofString(getEncodedMessages()));
		return httpClient.send(builder.build(), BodyHandlers.ofString());
	}
	
	public HttpResponse<String> sendGet() throws IOException, InterruptedException{
		Builder builder = HttpRequest.newBuilder(URI.create(messages.isEmpty() ? uri : (uri + "?" + getEncodedMessages())))
				.GET();
		headers.entrySet().stream().forEach(entry -> builder.header(entry.getKey(), entry.getValue()));
		return httpClient.send(builder.build(), BodyHandlers.ofString());		
	}

	private String getEncodedMessages() {
		List<String> messageList = messages.entrySet().stream().map(entry -> {
			try {
				return entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), encode);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return entry.toString();
		}).collect(Collectors.toList());
		return String.join("&", messageList);
	}
	
	public CookieHandler getCookieHandler() {
		return httpClient.cookieHandler().orElse(null);
	}
	
	public boolean isPresentCookieHandler() {
		return httpClient.cookieHandler().isPresent();
	}
}
