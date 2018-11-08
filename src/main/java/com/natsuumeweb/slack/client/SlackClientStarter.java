package com.natsuumeweb.slack.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.http.WebSocket;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.gson.Gson;
import com.natsuumeweb.api.SlackAPI;
import com.natsuumeweb.http.SimpleHttpClient;
import com.natsuumeweb.slack.data.ConnectionInfo;

public class SlackClientStarter implements Runnable{
	
	private ConnectionInfo connectionInfo;
	private String token;
	
	public SlackClientStarter(String token) {
		this.token = token;
	}

	public static void main(String[] args) {
		if(args.length != 1)
			return;
		
		SlackClientStarter starter = new SlackClientStarter(args[0]);
		ExecutorService exec = Executors.newSingleThreadExecutor();	
		exec.execute(starter);
	}
	
	private boolean connect() throws IOException, InterruptedException {
		Map<String, String> headers =  Map.of("Content-type", "application/x-www-form-urlencoded");
		Map<String, String> postMessages = Map.of("token", token);
		SimpleHttpClient httpClient = new SimpleHttpClient(SlackAPI.CONNECT.getURIText(), headers, postMessages);
		
		HttpResponse<String> response = httpClient.sendPost();
		
		Gson gson = new Gson();
		ConnectionInfo connectionInfo = gson.fromJson(response.body(), ConnectionInfo.class);
		this.connectionInfo = connectionInfo;
		
		System.out.println("connection " + connectionInfo.isSucceed());
		System.out.println(response.body());
		
		return connectionInfo.isSucceed();
	}
	
	@Override
	public void run() {
		boolean isConnected = false;
		
		try {
			isConnected = connect();
		} catch (IOException | InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(!isConnected) {
			System.err.println("SlackClient is not connected.");
			return;
		}
		HttpClient client = HttpClient.newHttpClient();
		CompletableFuture<WebSocket> future = client
				.newWebSocketBuilder()
				.buildAsync(URI.create(connectionInfo.getURI()), new SlackClient());
		while(true) {
			Thread.yield();
		}
	}
}
