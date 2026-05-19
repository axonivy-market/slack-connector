package com.axonivy.connector.slack.service;

import java.util.List;

import ch.ivyteam.ivy.security.exec.Sudo;
import ch.ivyteam.ivy.workflow.ICase;
import ch.ivyteam.ivy.workflow.query.CaseQuery;

public class TaskCaseService {

	public static List<ICase> getRunningCases() {
		return Sudo.get(() -> {
			return CaseQuery.businessCases().executor().results();
		});
	}
}
