package com.axonivy.connector.slack.service;

import java.util.HashMap;
import java.util.Map;

import ch.ivyteam.ivy.environment.Ivy;

public class MessageService {

	private static final String SEND_MESSAGE_CALLABLE = "sendBotMessage(java.lang.String, java.lang.String, java.lang.String)";

	public static void sendBotMessageToSlack(String channelId, String message) {
		Map<String, Object> params = new HashMap<>();
		params.put("channel", channelId);
		params.put("message", message);
		params.put("botToken", Ivy.var().get("com.axonivy.connector.slack.botToken"));
		IvyService.startSubProcessInApplication(SEND_MESSAGE_CALLABLE, "SendBotMessage", params);
	}
}
