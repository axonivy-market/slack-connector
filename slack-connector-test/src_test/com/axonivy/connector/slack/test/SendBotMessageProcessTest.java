package com.axonivy.connector.slack.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.axonivy.connector.slack.mock.SlackApiMock;

import ch.ivyteam.ivy.bpm.engine.client.BpmClient;
import ch.ivyteam.ivy.bpm.engine.client.ExecutionResult;
import ch.ivyteam.ivy.bpm.engine.client.element.BpmElement;
import ch.ivyteam.ivy.bpm.engine.client.element.BpmProcess;

public class SendBotMessageProcessTest extends BaseProcessTest {
	private static final BpmProcess SEND_BOT_MESSAGE_PROCESS = BpmProcess.path("SendBotMessage");
	private static final BpmElement SEND_BOT_MESSAGE_CALLABLE = SEND_BOT_MESSAGE_PROCESS
			.elementName("sendBotMessage(String,String,String)");

	@Test
	public void testSendBotMessage(BpmClient bpmClient) {
		ExecutionResult result = bpmClient.start().subProcess(SEND_BOT_MESSAGE_CALLABLE).execute("test", "channel",
				TEST_TOKEN);

		assertTrue(result.bpmError() == null);
		assertEquals(1, SlackApiMock.getCallCount());
	}
}
