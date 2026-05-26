package com.axonivy.connector.slack.service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.security.PermitAll;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.axonivy.connector.slack.dto.SlashCommandData;
import com.axonivy.connector.slack.enums.CustomField;
import com.axonivy.connector.slack.enums.SeverityLevel;

import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.workflow.ICase;

@Path("/incident")
@PermitAll
public class SlashCommandService {

	private static final String UNKNOWN = "Unknown";
	private static final String CREATE_INCIDENT_SIGNAL = "CreateIncident";

	@POST
	@Path("/create")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public String startProcess(@BeanParam SlashCommandData cmd) {
		String user = cmd.getUserName() != null ? cmd.getUserName() : UNKNOWN;
		try {
			if (cmd != null) {
				Ivy.wf().signals().create().data(cmd).makeCurrentTaskPersistent().send(CREATE_INCIDENT_SIGNAL);
			}
		} catch (Exception e) {
			Ivy.log().error("startProcess async failed", e);
		}
		return String.format("Process CreateIncident has been started by user: %s", user);
	}

	@POST
	@Path("/summary")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public String summaryIncidents() {
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
			return "No incidents found";
		}

		return counts.entrySet().stream().sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder()))
				.map(e -> e.getKey() + ": " + e.getValue()).collect(Collectors.joining(", ", "Incident summary: ", ""));
	}
}
