package com.handlers.saf.b2b;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import com.handlers.saf.b2b.models.B2BResponseObject;
import com.handlers.saf.global.Requests;
import com.handlers.saf.models.CredentialsObject;
import com.handlers.saf.models.PaymentRequestObject;
import com.handlers.saf.oauth.SafaricomOauth;
import com.handlers.saf.utilities.CredsManager;
import com.handlers.saf.utilities.HttpHelper;

@Service
public class B2BRequest  implements Requests{

	@Value("${app.safaricom.base.url}")
	private String baseURL;

	@Value("${app.safaricom.b2b.timeout.url}")
	private String timeoutURL;

	@Value("${app.safaricom.b2b.result.url}")
	private String resultURL;

	@Value("${app.safaricom.b2b.postfix.url}")
	private String postFixUrl;

	private String SHORTCODE_IDENTIFIER = "4";

	private String COMMAND_ID = "BusinessPayBill";

	@Autowired
	SafaricomOauth safaricomOauth;

	@Autowired
	HttpHelper httpHelper;

	@Autowired
	CredsManager credsManager;

	@Autowired
	B2BDatabaseHelper databaseHelper;

	
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

	private B2BResponseObject sendRequest(String url, String payload, Map<String, String> headers) {

		try {

			String response = httpHelper.postRequest(url, payload, headers);

			return processResponse(response);

		} catch (HttpClientErrorException e) {
			System.err.println("HTTP Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());

			JSONObject jo = new JSONObject(e.getResponseBodyAsString());
			var response = new B2BResponseObject();

			response.setResponseCode(jo.optString("errorCode"));
			response.setResponseDescription(jo.optString("errorMessage"));

			response.setRawPayload(e.getResponseBodyAsString());

			return response;
		}

		catch (Exception ex) {
			ex.printStackTrace();

			return new B2BResponseObject();

		}

	}

	private B2BResponseObject processResponse(String response) {

		B2BResponseObject respo = new B2BResponseObject();

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
		payload.put("Initiator", creds.getInitiatorName());
		payload.put("SecurityCredential", creds.getSecurityCredential());
		payload.put("CommandID", COMMAND_ID);
		payload.put("SenderIdentifierType", SHORTCODE_IDENTIFIER);
		payload.put("RecieverIdentifierType", SHORTCODE_IDENTIFIER);
		payload.put("Amount", request.getAmount());
		payload.put("PartyA", request.getSource());
		payload.put("PartyB", request.getDestination());
		payload.put("AccountReference", request.getReference());
		payload.put("Remarks", request.getDescription());
		payload.put("QueueTimeOutURL", timeoutURL);
		payload.put("ResultURL", resultURL);
		return payload.toString();
	}

}
