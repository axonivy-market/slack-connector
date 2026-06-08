package com.axonivy.connector.slack.dto;

import com.axonivy.connector.slack.constant.SlackSlashCommandConstant;
import com.fasterxml.jackson.annotation.JsonProperty;

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

	@JsonProperty(SlackSlashCommandConstant.COMMAND)
	private String command;
	@JsonProperty(SlackSlashCommandConstant.TEXT)
	private String text;
	@JsonProperty(SlackSlashCommandConstant.CHANNEL_ID)
	private String channelId;
	@JsonProperty(SlackSlashCommandConstant.CHANNEL_NAME)
	private String channelName;
	@JsonProperty(SlackSlashCommandConstant.USER_ID)
	private String userId;
	@JsonProperty(SlackSlashCommandConstant.USER_NAME)
	private String userName;
	@JsonProperty(SlackSlashCommandConstant.RESPONSE_URL)
	private String responseUrl;
	@JsonProperty(SlackSlashCommandConstant.TRIGGER_ID)
	private String triggerId;
	@JsonProperty(SlackSlashCommandConstant.TEAM_ID)
	private String teamId;
	@JsonProperty(SlackSlashCommandConstant.TEAM_DOMAIN)
	private String teamDomain;
	@JsonProperty(SlackSlashCommandConstant.API_APP_ID)
	private String apiAppId;
	@JsonProperty(SlackSlashCommandConstant.TOKEN)
	private String token;
	@JsonProperty(SlackSlashCommandConstant.IS_ENTERPRISE_INSTALL)
	private String isEnterpriseInstall;

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

	public String getIsEnterpriseInstall() {
		return isEnterpriseInstall;
	}

	public void setIsEnterpriseInstall(String isEnterpriseInstall) {
		this.isEnterpriseInstall = isEnterpriseInstall;
	}

}
