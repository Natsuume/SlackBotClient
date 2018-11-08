package com.natsuumeweb.slack.client;

import java.net.http.WebSocket;
import java.net.http.WebSocket.Listener;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SlackClient implements Listener{
	
	private ExecutorService exec = Executors.newCachedThreadPool();
	private List<CharSequence> messageParts = new ArrayList<>();
	private CompletableFuture<?> accumulatedMessage = new CompletableFuture<>();
	
	@Override
	public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last){

		messageParts.add(data);
		webSocket.sendText("test", false);
		webSocket.request(1);
		if(last) {
			processMessage(webSocket, String.join("", messageParts));
			messageParts = new ArrayList<>();
			accumulatedMessage.complete(null);
			CompletionStage<?> cf = accumulatedMessage;
			accumulatedMessage = new CompletableFuture<>();
			return cf;
		}
		return accumulatedMessage;
	}
	
	private void processMessage(WebSocket webSocket, String message) {
		System.out.println("debugPrint : " + message);
	}
	
	@Override
	public void onError(WebSocket webSocket, Throwable error) {
		error.printStackTrace();
	}
	
}
