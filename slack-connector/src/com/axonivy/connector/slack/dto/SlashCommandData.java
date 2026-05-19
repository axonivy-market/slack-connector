package com.axonivy.connector.slack.dto;

import javax.ws.rs.FormParam;

import com.axonivy.connector.slack.constant.SlackSlashCommandConstant;

public class SlashCommandData {
	public SlashCommandData() {
		super();
	}

	public SlashCommandData(String command, String text, String channelId, String channelName, String userId,
			String userName, String responseUrl, String triggerId, String teamId, String teamDomain) {
		super();
		this.command = command;
		this.text = text;
		this.channelId = channelId;
		this.channelName = channelName;
		this.userId = userId;
		this.userName = userName;
		this.responseUrl = responseUrl;
		this.triggerId = triggerId;
		this.teamId = teamId;
		this.teamDomain = teamDomain;
	}

	@FormParam(SlackSlashCommandConstant.COMMAND)
	private String command;
	@FormParam(SlackSlashCommandConstant.TEXT)
	private String text;
	@FormParam(SlackSlashCommandConstant.CHANNEL_ID)
	private String channelId;
	@FormParam(SlackSlashCommandConstant.CHANNEL_NAME)
	private String channelName;
	@FormParam(SlackSlashCommandConstant.USER_ID)
	private String userId;
	@FormParam(SlackSlashCommandConstant.USER_NAME)
	private String userName;
	@FormParam(SlackSlashCommandConstant.RESPONSE_URL)
	private String responseUrl;
	@FormParam(SlackSlashCommandConstant.TRIGGER_ID)
	private String triggerId;
	@FormParam(SlackSlashCommandConstant.TEAM_ID)
	private String teamId;
	@FormParam(SlackSlashCommandConstant.TEAM_DOMAIN)
	private String teamDomain;
	@FormParam(SlackSlashCommandConstant.API_APP_ID)
	private String apiAppId;
	@FormParam(SlackSlashCommandConstant.TOKEN)
	private String token;

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getResponseUrl() {
		return responseUrl;
	}

	public void setResponseUrl(String responseUrl) {
		this.responseUrl = responseUrl;
	}

	public String getTriggerId() {
		return triggerId;
	}

	public void setTriggerId(String triggerId) {
		this.triggerId = triggerId;
	}

	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	public String getTeamDomain() {
		return teamDomain;
	}

	public void setTeamDomain(String teamDomain) {
		this.teamDomain = teamDomain;
	}

}
