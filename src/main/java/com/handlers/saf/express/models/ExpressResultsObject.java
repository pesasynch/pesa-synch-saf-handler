package com.handlers.saf.express.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.handlers.saf.utilities.Utils;

import lombok.Data;

@Data
public class ExpressResultsObject {

	
	@JsonProperty("ResultCode")
	String resultCode;
	@JsonProperty("ResultDesc")
	String resultDesc;
	@JsonProperty("MerchantRequestID")
	String merchantRequestID;
	@JsonProperty("CheckoutRequestID")
	String checkoutRequestID;

	String rawPayload;

	String mnoReference;

	int paymentStatusCode;

	String paymentStatusDesc;
	
	public void setRawPayload(String rawPayload) {
		
		this.rawPayload = Utils.minimizeJson(rawPayload);
		
	}
	

}
