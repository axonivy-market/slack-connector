package com.axonivy.connector.slack.test.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.axonivy.connector.slack.mock.SlackApiMock;
import com.axonivy.connector.slack.service.MessageService;
import com.axonivy.connector.slack.test.BaseProcessTest;

import ch.ivyteam.ivy.bpm.engine.client.BpmClient;

public class MessageServiceTest extends BaseProcessTest{
	@Test
	public void testSendMessage(BpmClient bpmClient) throws NoSuchFieldException {
		MessageService.sendBotMessageToSlack("12345", "test message");
		assertEquals(1, SlackApiMock.getCallCount());
	}
}
