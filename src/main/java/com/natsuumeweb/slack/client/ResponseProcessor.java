package com.natsuumeweb.slack.client;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Flow.Processor;
import java.util.concurrent.Flow.Subscription;
import java.util.concurrent.SubmissionPublisher;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.natsuumeweb.niconico.NiconicoClient;
import com.natsuumeweb.slack.data.MessageType;
import com.natsuumeweb.slack.message.LogMessage;
import com.natsuumeweb.slack.message.ResponseMessage;
import com.natsuumeweb.slack.message.TalkMessage;
import com.natsuumeweb.slack.message.TransmissionMessage;

public class ResponseProcessor extends SubmissionPublisher<TransmissionMessage> implements Processor<String, TransmissionMessage> {
	private Subscription subscription;
	private List<String> activeChannels = Collections.synchronizedList(new ArrayList<>());
	private NiconicoClient niconicoClient = new NiconicoClient(this::sendMessage);
	private Gson gson = new Gson();
	
	
	@Override
	public void onSubscribe(Subscription subscription) {
		this.subscription = subscription;
		subscription.request(1);
	}

	@Override
	public void onNext(String message) {
		System.out.println("onMessage : " + message);
		MessageType type = convertType(message);
		
		switch(type) {
		case MESSAGE:
			processMessage(message);
			break;
		case LOG:
			processLog(message);
			break;
		default:
			break;
		}
		

		subscription.request(1);
	}
	
	private void sendMessage(String message) {
		activeChannels.parallelStream()
				.map(channel -> new TalkMessage(message, channel))
				.forEach(this::submit);
	}
	
	private void processLog(String message) {
		LogMessage log = gson.fromJson(message, LogMessage.class);
		if(log.isOk())
			return;
		System.err.println(log.getError());
	}
	
	private void processMessage(String message) {
		ResponseMessage response = gson.fromJson(message, ResponseMessage.class);
		String text = response.getText();
		if(text != null && text.startsWith("command:")) {
			processCommand(text.split("(^command): *")[1], response.getChannel());
			return;
		}
	}
	
	private void processCommand(String command, String channel) {
		String[] array = command.split(" ");
		switch(array[0]) {
		case "activate":
			switch(array[1]) {
			case "bot":
				activeChannels.add(channel);
				sendMessage("このchannelへの投稿を行います。");
				break;
			case "nicorepo":
				if(activeChannels.isEmpty())
					break;
				
				try {
					String email = array[2].split("\\|")[0].split(":")[1];
					niconicoClient.login(email, array[3]);
					niconicoClient.getMyRepoData();
				} catch (IOException | InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
			break;
		case "deactivate":
//			switch(array[1]) {
//			case "bot":
//				sendMessage("このchannelへの投稿を終了します。");
//				activeChannels.remove(channel);
//				break;
//			case "nicorepo":
//			}
			break;
		}
	}
	
	private MessageType convertType(String message) {
		try {
			InputStream inputStream = new ByteArrayInputStream(message.getBytes("utf-8"));
			JsonReader reader = new JsonReader(new InputStreamReader(inputStream,"utf-8"));
			reader.beginObject();
			while(reader.hasNext()) {
				switch(reader.nextName()) {
				case "type":
					return MessageType.toMessageType(reader.nextString());
				case "ok":
					return MessageType.LOG;
				}
				break;
			}
			inputStream.close();
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return MessageType.OTHER;
	}

	@Override
	public void onError(Throwable throwable) {
		// TODO Auto-generated method stub
		throwable.printStackTrace();
	}

	@Override
	public void onComplete() {
		// TODO Auto-generated method stub
		
	}

}
