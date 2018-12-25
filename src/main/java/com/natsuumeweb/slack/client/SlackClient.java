package com.natsuumeweb.slack.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.http.WebSocket;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.google.gson.Gson;
import com.natsuumeweb.api.SlackAPI;
import com.natsuumeweb.http.SimpleHttpClient;
import com.natsuumeweb.slack.data.ConnectionInfo;

public class SlackClient{
	
	private ConnectionInfo connectionInfo;
	private SlackListener listener = new SlackListener();
	private ResponseProcessor processor = new ResponseProcessor();
	private SlackSpeaker speaker;
	private String token;

	public SlackClient(String token) {
		this.token = token;
	}

	public static void main(String[] args) {
		if(args.length != 1)
			return;
		SlackClient client = new SlackClient(args[0]);
		try {
			client.start();
		} catch (IOException | InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private boolean connect() throws IOException, InterruptedException {
		Map<String, String> headers =  Map.of("Content-type", "application/x-www-form-urlencoded");
		Map<String, String> postMessages = Map.of("token", token);
		SimpleHttpClient httpClient = new SimpleHttpClient(SlackAPI.CONNECT.getURIText(), headers, postMessages);
		
		HttpResponse<String> response = httpClient.sendPost();
		
		Gson gson = new Gson();
		ConnectionInfo connectionInfo = 
				gson.fromJson(response.body(), ConnectionInfo.class);
		this.connectionInfo = connectionInfo;
		System.out.println(gson.toJson(connectionInfo));
		
		return connectionInfo.isSucceed();
	}
	
	private WebSocket createWebSocket() throws InterruptedException, ExecutionException {
		HttpClient client = HttpClient.newHttpClient();
		CompletableFuture<WebSocket> future = client
				.newWebSocketBuilder()
				.buildAsync(URI.create(connectionInfo.getURI()), listener);
		return future.get();
	}
	
	public void start() throws IOException, InterruptedException, ExecutionException {
		connect();
		
		speaker = new SlackSpeaker(createWebSocket());
		listener.subscribe(processor);
		processor.subscribe(speaker);
		
		while(true) {
			speaker.sendPing();
			Thread.sleep(1000);;
		}
	}
}
