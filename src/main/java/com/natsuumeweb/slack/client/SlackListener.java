package com.natsuumeweb.slack.client;

import java.net.http.WebSocket;
import java.net.http.WebSocket.Listener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.SubmissionPublisher;

public class SlackListener extends SubmissionPublisher<String> implements Listener{
	private List<CharSequence> messageParts = new ArrayList<>();
	private CompletableFuture<?> accumulatedMessage = new CompletableFuture<>();
	
	@Override
	public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last){
		messageParts.add(data);
		webSocket.request(1);
		if(last) {
			submit(String.join("", messageParts));
			messageParts = new ArrayList<>();
			accumulatedMessage.complete(null);
			CompletionStage<?> cf = accumulatedMessage;
			accumulatedMessage = new CompletableFuture<>();
			return cf;
		}
		return accumulatedMessage;
	}
	
	@Override
	public void onError(WebSocket webSocket, Throwable error) {
		error.printStackTrace();
	}
}
