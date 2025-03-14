package com.handlers.saf.express;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import com.handlers.saf.express.models.ExpressResponseObject;
import com.handlers.saf.global.Requests;
import com.handlers.saf.models.CredentialsObject;
import com.handlers.saf.models.PaymentRequestObject;
import com.handlers.saf.oauth.SafaricomOauth;
import com.handlers.saf.utilities.CredsManager;
import com.handlers.saf.utilities.HttpHelper;

@Service
public class ExpressRequest implements Requests {

	@Value("${app.safaricom.base.url}")
	private String baseURL;

	@Value("${app.safaricom.express.postfix.url}")
	private String postFixUrl;

	@Value("${app.safaricom.express.callback.url}")
	private String callbackURL;

	@Autowired
	SafaricomOauth safaricomOauth;

	@Autowired
	HttpHelper httpHelper;

	@Autowired
	CredsManager credsManager;

	@Autowired
	ExpressDatabaseHelper databaseHelper;

	private String TRANSACTION_TYPE = "CustomerPayBillOnline";

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

	public ExpressResponseObject sendRequest(String url, String payload, Map<String, String> headers) {

		try {

			String response = httpHelper.postRequest(url, payload, headers);

			return processResponse(response);

		} catch (HttpClientErrorException e) {
			System.err.println("HTTP Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());

			JSONObject jo = new JSONObject(e.getResponseBodyAsString());
			var response = new ExpressResponseObject();

			response.setResponseCode(jo.optString("errorCode"));
			response.setResponseDescription(jo.optString("errorMessage"));

			response.setRawPayload(e.getResponseBodyAsString());

			return response;
		}

		catch (Exception ex) {
			ex.printStackTrace();

			return new ExpressResponseObject();

		}

	}

	private String createPayload(CredentialsObject creds, PaymentRequestObject request) {

		var timestamp = getTimestamp();
		JSONObject payload = new JSONObject();
		payload.put("BusinessShortCode", request.getDestination());
		payload.put("Password", constructPassword(request.getDestination(), creds.getPassKey(), timestamp));
		payload.put("Timestamp", timestamp);
		payload.put("TransactionType", TRANSACTION_TYPE);
		payload.put("Amount", request.getTotalAmount());
		payload.put("PartyA", request.getSource());
		payload.put("PartyB", request.getDestination());
		payload.put("PhoneNumber", request.getSource());
		payload.put("CallBackURL", callbackURL);
		payload.put("AccountReference", request.getReference());
		payload.put("TransactionDesc", request.getDescription());
		return payload.toString();
	}

	private ExpressResponseObject processResponse(String response) {

		ExpressResponseObject respo = new ExpressResponseObject();

		JSONObject responseObject = new JSONObject(response);

		respo.setMerchantRequestID(responseObject.optString("MerchantRequestID"));
		respo.setCheckoutRequestID(responseObject.optString("CheckoutRequestID"));
		respo.setResponseCode(responseObject.optString("ResponseCode"));
		respo.setResponseDescription(responseObject.optString("ResponseDescription"));
		respo.setCustomerMessage(responseObject.optString("CustomerMessage"));

		// Set payload
		respo.setRawPayload(response);

		return respo;

	}

	private String constructPassword(String shortcode, String passkey, String timestamp) {
		return Base64.getEncoder().encodeToString((shortcode + passkey + timestamp).getBytes());
	}

	private String getTimestamp() {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		return LocalDateTime.now().format(formatter);
	}

}
