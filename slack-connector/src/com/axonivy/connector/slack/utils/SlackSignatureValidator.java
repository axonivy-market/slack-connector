package com.axonivy.connector.slack.utils;

import com.slack.api.app_backend.SlackSignature;

import ch.ivyteam.ivy.environment.Ivy;

public class SlackSignatureValidator {

	public static boolean validateSlackRequest(String timestamp, String requestBody, String slackSignature) {
		String signingSecret = Ivy.var().get("com.axonivy.connector.slack.signingSecret").trim();
		SlackSignature.Generator generator = new SlackSignature.Generator(signingSecret);
		SlackSignature.Verifier verifier = new SlackSignature.Verifier(generator);

		String generated = generator.generate(timestamp, requestBody);

		Ivy.log().info("Slack signature: " + slackSignature);
		Ivy.log().info("Generated signature: " + generated);

		return verifier.isValid(timestamp, requestBody, slackSignature);
	}
}
