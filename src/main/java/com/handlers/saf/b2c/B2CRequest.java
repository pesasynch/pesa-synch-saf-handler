package com.handlers.saf.b2c;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import com.handlers.saf.b2c.models.B2CResponseObject;
import com.handlers.saf.global.Requests;
import com.handlers.saf.models.CredentialsObject;
import com.handlers.saf.models.PaymentRequestObject;
import com.handlers.saf.oauth.SafaricomOauth;
import com.handlers.saf.utilities.CredsManager;
import com.handlers.saf.utilities.HttpHelper;

@Service
public class B2CRequest implements Requests{

	@Value("${app.safaricom.base.url}")
	private String baseURL;

	@Value("${app.safaricom.b2c.timeout.url}")
	private String timeoutURL;

	@Value("${app.safaricom.b2c.result.url}")
	private String resultURL;

	@Value("${app.safaricom.b2c.postfix.url}")
	private String postFixUrl;

	private String COMMAND_ID = "BusinessPayment";

	@Autowired
	SafaricomOauth safaricomOauth;

	@Autowired
	HttpHelper httpHelper;

	@Autowired
	CredsManager credsManager;

	@Autowired
	B2CDatabaseHelper databaseHelper;

	
	@Override
	public void init(PaymentRequestObject request) {

		// Get creds
		CredentialsObject creds = credsManager.getCreds(request.getSource());

		// Get token
		String token = safaricomOauth.getOAuthToken(creds.getConsumerKey(), creds.getConsumerSecret());

		// Init payments
		Map<String, String> headers = new HashMap<>();
		headers.put("Authorization", "Bearer " + token);

		// Save into the database
		var saved = databaseHelper.insertPayment(request);

		String payload = createPayload(creds, request);
		String url = baseURL + postFixUrl;

		var processed = sendRequest(url, payload, headers);

		databaseHelper.updateResponse(saved, processed);

	}

	private B2CResponseObject sendRequest(String url, String payload, Map<String, String> headers) {

		try {

			String response = httpHelper.postRequest(url, payload, headers);

			return processResponse(response);

		} catch (HttpClientErrorException e) {
			System.err.println("HTTP Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());

			JSONObject jo = new JSONObject(e.getResponseBodyAsString());
			var response = new B2CResponseObject();

			response.setResponseCode(jo.optString("errorCode"));
			response.setResponseDescription(jo.optString("errorMessage"));

			response.setRawPayload(e.getResponseBodyAsString());

			return response;
		}

		catch (Exception ex) {
			ex.printStackTrace();

			return new B2CResponseObject();

		}

	}

	private B2CResponseObject processResponse(String response) {

		B2CResponseObject respo = new B2CResponseObject();

		JSONObject responseObject = new JSONObject(response);

		respo.setConversationID(responseObject.optString("ConversationID"));
		respo.setOriginatorConversationID(responseObject.optString("OriginatorConversationID"));
		respo.setResponseCode(responseObject.optString("ResponseCode"));
		respo.setResponseDescription(responseObject.optString("ResponseDescription"));

		// Set payload
		respo.setRawPayload(response);

		return respo;

	}

	private String createPayload(CredentialsObject creds, PaymentRequestObject request) {

		JSONObject payload = new JSONObject();
		payload.put("OriginatorConversationID", request.getReference());
		payload.put("InitiatorName", creds.getInitiatorName());
		payload.put("SecurityCredential", creds.getSecurityCredential());
		payload.put("CommandID", COMMAND_ID);
		payload.put("Amount", request.getAmount());
		payload.put("PartyA", request.getSource());
		payload.put("PartyB", request.getDestination());
		payload.put("Remarks", request.getDescription());
		payload.put("QueueTimeOutURL", timeoutURL);
		payload.put("ResultURL", resultURL);
		payload.put("Occassion", "");
		return payload.toString();
	}

}
