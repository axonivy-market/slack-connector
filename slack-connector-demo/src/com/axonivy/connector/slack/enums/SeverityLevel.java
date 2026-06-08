package com.axonivy.connector.slack.enums;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

public enum SeverityLevel {
	LOW("Low"), MEDIUM("Medium"), HIGH("High"), CRITICAL("Critical"), UNKNOWN("Unknown");

	private final String value;

	SeverityLevel(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

	public static SeverityLevel fromValue(String value) {
		if (StringUtils.isBlank(value)) {
			return UNKNOWN;
		}

		return Arrays.stream(values()).filter(level -> level.value.equalsIgnoreCase(value)).findFirst().orElse(UNKNOWN);
	}
}
