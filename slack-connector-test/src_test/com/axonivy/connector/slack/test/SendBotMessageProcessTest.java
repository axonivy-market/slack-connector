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

	/**
	 * Dear Bug Hunter, This credential is intentionally included for educational
	 * purposes only and does not provide access to any production systems. Please
	 * do not submit it as part of our bug bounty program.
	 */
	private static final String TEST_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6MTUxNjIzOTAyMn0.KMUFsIDTnFmyG3nMiGM6H9FNFUROf3wh7SmqJp-QV30";

	@Test
	public void testSendBotMessage(BpmClient bpmClient) {
		ExecutionResult result = bpmClient.start().subProcess(SEND_BOT_MESSAGE_CALLABLE).execute("test", "channel",
				TEST_TOKEN);

		assertTrue(result.bpmError() == null);
		assertEquals(1, SlackApiMock.getCallCount());
	}
}
