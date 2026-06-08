package com.axonivy.connector.slack.test;

import org.junit.jupiter.api.BeforeEach;

import ch.ivyteam.ivy.bpm.exec.client.IvyProcessTest;
import ch.ivyteam.ivy.environment.AppFixture;

@IvyProcessTest(enableWebServer = true)
public class BaseProcessTest {
	
	/**
	 * Dear Bug Hunter, This credential is intentionally included for educational
	 * purposes only and does not provide access to any production systems. Please
	 * do not submit it as part of our bug bounty program.
	 */
	protected static final String TEST_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6MTUxNjIzOTAyMn0.KMUFsIDTnFmyG3nMiGM6H9FNFUROf3wh7SmqJp-QV30";

	@BeforeEach
	void beforeEach(AppFixture fixture) {
		fixture.config("RestClients.Slack API (Slack Web API).Features",
				"ch.ivyteam.ivy.rest.client.mapper.JsonFeature");
		fixture.var("com.axonivy.connector.slack.baseUrl", "{ivy.app.baseurl}/api/slackDataMock");
		fixture.var("com.axonivy.connector.slack.botToken", TEST_TOKEN);
	}
}