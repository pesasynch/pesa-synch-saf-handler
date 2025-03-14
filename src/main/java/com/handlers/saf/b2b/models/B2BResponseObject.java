package com.handlers.saf.b2b.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.handlers.saf.utilities.Utils;

import lombok.Data;

@Data
public class B2BResponseObject {

	@JsonProperty("OriginatorConversationID")
	String originatorConversationID;

	@JsonProperty("ConversationID")
	String conversationID;

	@JsonProperty("ResponseCode")
	String responseCode;

	@JsonProperty("ResponseDescription")
	String responseDescription;

	String rawPayload;

	int paymentStatusCode;

	String paymentStatusDesc;

	public void setRawPayload(String rawPayload) {

		this.rawPayload = Utils.minimizeJson(rawPayload);

	}

}
