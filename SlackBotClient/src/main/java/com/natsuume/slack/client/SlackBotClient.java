package com.natsuume.slack.client;

import java.io.IOException;
import java.net.URI;

import jdk.incubator.http.HttpClient;
import jdk.incubator.http.HttpRequest;
import jdk.incubator.http.HttpResponse;

public class SlackBotClient {
	
	private final URI uri;
	
	public SlackBotClient(String url) {
		this.uri = URI.create(url);
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
				.POST(HttpRequest.BodyPublisher.fromString(jsonContent))
				.build();
		try {
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandler.asString());
			System.out.println(response.headers());
			System.out.println(response.statusCode());
			System.out.println(response.body());
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
