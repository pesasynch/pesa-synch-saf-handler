package com.handlers.saf.models;

import lombok.Data;

@Data
public class PaymentRequestObject {

	private String serviceCode;

	private int amount = 0;

	private int charge = 0;

	private String currency = "KES";

	private String source;

	private String destination;

	private String reference;
	
	private String description;

	private String paymentUuid;

	public int getTotalAmount() {
		return amount + charge;
	}

}
