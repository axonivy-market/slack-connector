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

import org.apache.commons.lang3.StringUtils;

import com.axonivy.connector.slack.dto.SlashCommandData;

import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.workflow.ICase;

@Path("/incident")
@PermitAll
public class SlashCommandService {

	@POST
	@Path("/create")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public String startProcess(@BeanParam SlashCommandData cmd) {
		Ivy.log().error(cmd.getUserName());
		String user = cmd.getUserName() != null ? cmd.getUserName() : "unknown";
		try {
			if (cmd != null) {
				Ivy.wf().signals().create().data(cmd).makeCurrentTaskPersistent().send("CreateIncident");
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
		Map<String, Integer> counts = cases.stream().map(c -> { // map case -> severity (default "Unspecified")
			try {
				if (c.customFields() != null) {
					String s = c.customFields().stringField("severity").getOrNull();
					return StringUtils.isBlank(s) ? "Unspecified" : s;
				}
			} catch (Exception ignored) {
			}
			return "Unspecified";
		}).collect(Collectors.groupingBy(Function.identity(), Collectors.summingInt(e -> 1))); // Map<String,Integer>

		if (counts.isEmpty()) {
			return "No incidents found";
		}

		// build summary sorted by count desc
		return counts.entrySet().stream().sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder()))
				.map(e -> e.getKey() + ": " + e.getValue()).collect(Collectors.joining(", ", "Incident summary: ", ""));
	}
}
