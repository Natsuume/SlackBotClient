package com.natsuumeweb.slack.client;

import java.net.http.WebSocket;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;

import com.google.gson.Gson;
import com.natsuumeweb.slack.message.PingMessage;
import com.natsuumeweb.slack.message.TransmissionMessage;

public class SlackSpeaker implements Subscriber<TransmissionMessage>{
	private WebSocket webSocket;
	private Gson gson = new Gson();
	private int messageId = 0;
	private Subscription subscription;
	
	public SlackSpeaker(WebSocket webSocket) {
		this.webSocket = webSocket;
	}
	
	public void sendPing() {
		sendMessage(new PingMessage("ping"));
	}
	
	private synchronized void sendMessage(TransmissionMessage message) {
		if(messageId == Integer.MAX_VALUE)
			messageId = 0;
		
		message.setId(messageId++);
		webSocket.sendText(gson.toJson(message), true);
	}

	@Override
	public void onSubscribe(Subscription subscription) {
		this.subscription = subscription;
		subscription.request(1);
	}

	@Override
	public void onNext(TransmissionMessage message) {
		sendMessage(message);
		subscription.request(1);
	}

	@Override
	public void onError(Throwable throwable) {
		throwable.printStackTrace();	
	}

	@Override
	public void onComplete() {
		
	}
}
