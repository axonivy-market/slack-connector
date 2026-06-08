package com.axonivy.connector.slack.service;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import com.axonivy.connector.slack.dto.SlashCommandData;
import com.axonivy.connector.slack.enums.CustomField;
import com.axonivy.connector.slack.enums.SeverityLevel;
import com.axonivy.connector.slack.utils.SlackSignatureValidator;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.workflow.ICase;

@Path("/incident")
@PermitAll
public class SlashCommandService {

	private static final String UNKNOWN = "Unknown";
	private static final String CREATE_INCIDENT_SIGNAL = "CreateIncident";
	private final ObjectMapper mapper = new ObjectMapper();
	private static final String INVALID_SIGNATURE = "Invalid Slack signature";

	@POST
	@Path("/create")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN)
	public String startProcess(@Context HttpHeaders headers, String rawBody) {
		if (!validateSlackRequest(headers, rawBody)) {
			return INVALID_SIGNATURE;
		}
		SlashCommandData cmd = parseCommand(rawBody);
		String user = cmd.getUserName() != null ? cmd.getUserName() : UNKNOWN;
		try {
			Ivy.wf().signals().create().data(cmd).makeCurrentTaskPersistent().send(CREATE_INCIDENT_SIGNAL);
		} catch (Exception e) {
			Ivy.log().error("startProcess async failed", e);
			return "Failed to create incident";
		}

		return String.format("Process CreateIncident has been started by user: %s", user);
	}

	@POST
	@Path("/summary")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN)
	public String summaryIncidents(@Context HttpHeaders headers, String rawBody) {
		if (!validateSlackRequest(headers, rawBody)) {
			return "Invalid Slack signature";
		}
		List<ICase> cases = TaskCaseService.getRunningCases();
		Map<String, Integer> counts = cases.stream().map(c -> {
			try {
				if (c.customFields() != null) {
					return c.customFields().stringField(CustomField.SEVERITY.getFieldName())
							.getOrDefault(SeverityLevel.UNKNOWN.getValue());
				}
			} catch (Exception ignored) {
			}
			return SeverityLevel.UNKNOWN.getValue();

		}).collect(Collectors.groupingBy(Function.identity(), Collectors.summingInt(e -> 1)));

		if (counts.isEmpty()) {
			return INVALID_SIGNATURE;
		}

		return counts.entrySet().stream().sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder()))
				.map(e -> e.getKey() + ": " + e.getValue()).collect(Collectors.joining(", ", "Incident summary: ", ""));
	}

	private boolean validateSlackRequest(HttpHeaders headers, String rawBody) {
		String timestamp = headers.getHeaderString("X-Slack-Request-Timestamp");
		String signature = headers.getHeaderString("X-Slack-Signature");
		return SlackSignatureValidator.validateSlackRequest(timestamp, rawBody, signature);
	}

	private SlashCommandData parseCommand(String rawBody) {
		try {
			Map<String, String> formMap = Arrays.stream(rawBody.split("&")).map(s -> s.split("=", 2))
					.collect(Collectors.toMap(s -> URLDecoder.decode(s[0], StandardCharsets.UTF_8),
							s -> s.length > 1 ? URLDecoder.decode(s[1], StandardCharsets.UTF_8) : ""));
			return mapper.convertValue(formMap, SlashCommandData.class);
		} catch (Exception e) {
			Ivy.log().error("Failed to parse slash command", e);
			return new SlashCommandData();
		}
	}
}