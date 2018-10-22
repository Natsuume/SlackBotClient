package com.natsuume.slack.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SlackBotClient {
	
	private final URI uri;
	
	public SlackBotClient(String url) {
		this.uri = URI.create("https://hooks.slack.com/services/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");
	}
	
	public static void main(String[] args) {
		if(args.length != 1)
			return;
		new SlackBotClient(args[0]).sendMessage("Hello, World!");
	}
	
	public void sendMessage(String content) {
		String jsonContent = "{\"text\":\"Hello, World!\"}";
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder(uri)
				.header("Content-type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(jsonContent))
				.build();
		try {
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			System.out.println(response.headers());
			System.out.println(response.statusCode());
			System.out.println(response.body());
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
