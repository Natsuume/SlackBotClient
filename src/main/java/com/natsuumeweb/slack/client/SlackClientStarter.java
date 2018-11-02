package com.natsuumeweb.slack.client;

import java.io.IOException;

public class SlackClientStarter {

	
	public static void main(String[] args) {
		if(args.length != 1)
			return;
		
		String token = args[0];
		SlackClient client = new SlackClient();
		try {
			client.connect(token);
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
