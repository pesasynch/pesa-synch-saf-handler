package com.handlers.saf.b2b.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.handlers.saf.utilities.Utils;

import lombok.Data;

@Data
public class B2BResultsObject {

	@JsonProperty("ResultType")
	String resultType;
	@JsonProperty("ResultCode")
	String resultCode;
	@JsonProperty("ResultDesc")
	String resultDesc;
	@JsonProperty("OriginatorConversationID")
	String originatorConversationID;
	@JsonProperty("ConversationID")
	String conversationId;
	@JsonProperty("TransactionID")
	String transactionId;

	String rawPayload;

	String mnoReference;

	Double accountBalance = 0.0;

	String receiverName;

	int paymentStatusCode;

	String paymentStatusDesc;

	public void setRawPayload(String rawPayload) {

		this.rawPayload = Utils.minimizeJson(rawPayload);

	}

}
