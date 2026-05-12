package com.axonivy.connector.slack.connector;

import java.util.Arrays;

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
		if (value == null || value.isBlank()) {
			return UNKNOWN;
		}
		
		return Arrays.stream(values())
				.filter(level -> level.value.equalsIgnoreCase(value) || level.name().equalsIgnoreCase(value))
				.findFirst().orElse(UNKNOWN);
	}
}
