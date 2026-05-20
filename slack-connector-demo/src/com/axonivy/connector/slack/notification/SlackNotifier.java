package com.axonivy.connector.slack.notification;

import org.apache.commons.lang3.StringUtils;

import com.axonivy.connector.slack.enums.CustomField;
import com.axonivy.connector.slack.listener.NewTaskAssignmentListener;
import com.axonivy.connector.slack.service.MessageService;

import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.workflow.ITask;
import ch.ivyteam.ivy.workflow.IWorkflowManager;

public class SlackNotifier extends NewTaskAssignmentListener {

	private static final String NEW_TASK_MESSAGE = "An Axon Ivy task has been created successfully.\r\n"
			+ "Click %s to open the task in a simple UI and either reject or confirm the request";

	public SlackNotifier() {
		super(IWorkflowManager.instance());
		taskHandler(this::notifyNewTask);
	}

	private boolean isEnabled() {
		return Boolean.parseBoolean((Ivy.var().get("com.axonivy.connector.slack.notification.enabled")));
	}

	public void notifyNewTask(ITask newTask) {
		if (!isEnabled()) {
			return;
		}
		Ivy.log().info("notify new task clients on new task " + newTask);
		notifyToChannel(newTask);
	}

	private void notifyToChannel(ITask newTask) {
		String channel = newTask.getCase().customFields().stringField(CustomField.CHANNEL.getFieldName())
				.getOrDefault(StringUtils.EMPTY);

		MessageService.sendBotMessageToSlack(channel, prepareIncomingWebhookParameter(newTask, channel));
	}

	private String prepareIncomingWebhookParameter(ITask newTask, String channel) {
		String newTaskLinkTxt = String.format("<%s|%s>", newTask.getStartLink().getAbsolute(), newTask.getName());
		return String.format(NEW_TASK_MESSAGE, newTaskLinkTxt);
	}
}