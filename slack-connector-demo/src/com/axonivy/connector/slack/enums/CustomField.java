package com.axonivy.connector.slack.enums;

public enum CustomField {
	CHANNEL("channel"), SEVERITY("severity");

	private final String fieldName;

	private CustomField(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldName() {
		return fieldName;
	}
}
