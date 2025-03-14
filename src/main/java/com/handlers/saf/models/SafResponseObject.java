package com.handlers.saf.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;


@Data
public class SafResponseObject {
	
	
	@JsonProperty("ResultCode")	
	String resultCode = "0";
	
	@JsonProperty("ResultDesc")	
	String resultDesc = "success";

}
