package com.handlers.saf.utilities;

public enum PaymentStatus {

	CREATED(0, "CREATED"), INITIATED(1, "INITIATED"), AWAITING(2, "AWAITING"), SUCCESS(3, "SUCCESS"),
	FAILED(4, "FAILED"), ESCALATED(5, "ESCALATED");

	private final int code;
	private final String description;

	PaymentStatus(int code, String description) {
		this.code = code;
		this.description = description;
	}

	public int getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

}
