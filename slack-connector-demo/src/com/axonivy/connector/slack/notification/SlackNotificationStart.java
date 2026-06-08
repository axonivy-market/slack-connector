package com.axonivy.connector.slack.notification;

import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.process.eventstart.AbstractProcessStartEventBean;
import ch.ivyteam.ivy.service.ServiceException;
import ch.ivyteam.ivy.workflow.IWorkflowManager;

public class SlackNotificationStart extends AbstractProcessStartEventBean {

	private SlackNotifier listener;

	public SlackNotificationStart() {
		super("SlackNotificationStart", "Listenner to trigger a notification to Slack when a Task is created");
	}

	@Override
	public void poll() {
		getEventBeanRuntime().poll().disable();
	}

	@Override
	public void start() throws ServiceException {
		super.start();

		IWorkflowManager wfManager = IWorkflowManager.instance();
		this.listener = new SlackNotifier();
		wfManager.addWorkflowListener(listener);
		Ivy.log().info("Slack-notification installed");
	}

	@Override
	public void stop() throws ServiceException {
		if (this.listener != null) {
			IWorkflowManager.instance().removeWorkflowListener(listener);
			Ivy.log().info("Slack-notification stopped");
			this.listener = null;
		}
		super.stop();
	}

}
