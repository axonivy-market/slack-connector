package com.axonivy.connector.slack.service;

import org.apache.commons.lang3.StringUtils;

import com.axonivy.connector.slack.constant.SlackSlashCommandConstant;
import com.axonivy.connector.slack.dto.SlackSlashCommand;

import ch.ivyteam.ivy.environment.Ivy;

public class SlackSlashCommandService {

	public SlackSlashCommand fromCurrentRequest() {
		SlackSlashCommand command = new SlackSlashCommand();
		command.setCommand(getParameter(SlackSlashCommandConstant.COMMAND));
		command.setText(getParameter(SlackSlashCommandConstant.TEXT));
		command.setChannelId(getParameter(SlackSlashCommandConstant.CHANNEL_ID));
		command.setChannelName(getParameter(SlackSlashCommandConstant.CHANNEL_NAME));
		command.setUserId(getParameter(SlackSlashCommandConstant.USER_ID));
		command.setUserName(getParameter(SlackSlashCommandConstant.USER_NAME));
		command.setResponseUrl(getParameter(SlackSlashCommandConstant.RESPONSE_URL));
		command.setTriggerId(getParameter(SlackSlashCommandConstant.TRIGGER_ID));
		command.setTeamId(getParameter(SlackSlashCommandConstant.TEAM_ID));
		command.setTeamDomain(getParameter(SlackSlashCommandConstant.TEAM_DOMAIN));

		return command;
	}

	private String getParameter(String name) {
		var parameter = Ivy.request().getParameter(name);

		if (parameter == null) {
			return null;
		}

		if (parameter instanceof java.util.List<?> list && !list.isEmpty()) {
			return String.valueOf(list.get(0));
		}

		if (parameter.getClass().isArray()) {
			Object[] array = (Object[]) parameter;
			return array.length > 0 ? String.valueOf(array[0]) : null;
		}

		return String.valueOf(parameter).replace("[", StringUtils.EMPTY).replace("]", StringUtils.EMPTY);
	}
}
