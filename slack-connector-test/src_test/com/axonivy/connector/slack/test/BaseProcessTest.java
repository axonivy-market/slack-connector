package com.axonivy.connector.slack.test;

import org.junit.jupiter.api.BeforeEach;

import ch.ivyteam.ivy.bpm.exec.client.IvyProcessTest;
import ch.ivyteam.ivy.environment.AppFixture;
import ch.ivyteam.ivy.environment.Ivy;

@IvyProcessTest(enableWebServer = true)
public class BaseProcessTest {

	@BeforeEach
	void beforeEach(AppFixture fixture) {
		fixture.config("RestClients.Slack API (Slack Web API).Features",
				"ch.ivyteam.ivy.rest.client.mapper.JsonFeature");
		fixture.var("com.axonivy.connector.slack.baseUrl", "{ivy.app.baseurl}/api/slackDataMock");
	}
}