package com.natsuumeweb.slack.client;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.http.WebSocket;
import java.util.Map;

import com.google.gson.Gson;
import com.natsuumeweb.api.SlackAPI;
import com.natsuumeweb.http.SimpleHttpClient;
import com.natsuumeweb.slack.info.ConnectionInfo;

public class SlackClient implements Runnable{
	
	private ConnectionInfo connectionInfo;

	public boolean connect(String botToken) throws IOException, InterruptedException {
		Map<String, String> headers =  Map.of("Content-type", "application/x-www-form-urlencoded");
		Map<String, String> postMessages = Map.of("token", botToken);
		SimpleHttpClient httpClient = new SimpleHttpClient(SlackAPI.CONNECT.getURIText(), headers, postMessages);
		
		HttpResponse<String> response = httpClient.sendPost();
		
		Gson gson = new Gson();
		ConnectionInfo connectionInfo = gson.fromJson(response.body(), ConnectionInfo.class);
		this.connectionInfo = connectionInfo;
		
		System.out.println("connection " + connectionInfo.isSucceed());
		
		return connectionInfo.isSucceed();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		if(connectionInfo == null || !connectionInfo.isSucceed()) {
			System.err.println("SlackClient is not connected.");
			return;
		}
		
		WebSocket webSocket;
		
	}
}
