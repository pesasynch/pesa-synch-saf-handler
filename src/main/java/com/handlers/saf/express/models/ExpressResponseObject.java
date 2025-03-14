package com.handlers.saf.express.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.handlers.saf.utilities.Utils;

import lombok.Data;

@Data
public class ExpressResponseObject {

	@JsonProperty("MerchantRequestID")
	private String merchantRequestID;
	@JsonProperty("CheckoutRequestID")
	private String checkoutRequestID;
	@JsonProperty("ResponseCode")
	private String responseCode;
	@JsonProperty("ResponseDescription")
	private String responseDescription;
	@JsonProperty("CustomerMessage")
	private String customerMessage;

	String rawPayload;

	int paymentStatusCode;

	String paymentStatusDesc;

	public void setRawPayload(String rawPayload) {

		this.rawPayload = Utils.minimizeJson(rawPayload);

	}
}
