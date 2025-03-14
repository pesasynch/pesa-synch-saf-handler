package com.handlers.saf.express;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.handlers.saf.exceptions.DuplicateCallbackException;
import com.handlers.saf.express.models.ExpressResultsObject;
import com.handlers.saf.global.Callbacks;
import com.handlers.saf.models.SafResponseObject;
import com.handlers.saf.utilities.PaymentStatus;
import com.handlers.saf.utilities.Utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/safaricom/express")
public class ExpressCallbacks extends Callbacks {

	@Autowired
	ExpressDatabaseHelper databaseHelper;

	@PostMapping("/callback")
	public ResponseEntity<SafResponseObject> processCallbacks(@RequestBody String payload) {

		log.info("Received express callback : ", payload);

		// Process results
		try {
			databaseHelper.updateResults(processResults(payload));
		} catch (DuplicateCallbackException e) {
			log.info("Duplicate callback : ", e.getMessage());
		}

		return ResponseEntity.status(200).body(new SafResponseObject());
	}

	private ExpressResultsObject processResults(String payload) throws DuplicateCallbackException {

		JSONObject body = new JSONObject(payload).getJSONObject("Body");
		JSONObject results = body.getJSONObject("stkCallback");

		ExpressResultsObject respo = new ExpressResultsObject();
		// Basic
		respo.setResultCode(results.optString("ResultCode"));
		respo.setResultDesc(results.optString("ResultDesc"));
		respo.setMerchantRequestID(results.optString("MerchantRequestID"));
		respo.setCheckoutRequestID(results.optString("CheckoutRequestID"));

		duplicatesChecker(respo.getCheckoutRequestID());

		// Success
		if ("0".equalsIgnoreCase(respo.getResultCode())) {
			var meta = Utils.extractMetadata(results.get("CallbackMetadata").toString(), "Item");

			respo.setMnoReference(meta.getOrDefault("MpesaReceiptNumber", ""));

			respo.setPaymentStatusCode(PaymentStatus.SUCCESS.getCode());
			respo.setPaymentStatusDesc(PaymentStatus.SUCCESS.getDescription());

		} else {
			respo.setPaymentStatusCode(PaymentStatus.FAILED.getCode());
			respo.setPaymentStatusDesc(PaymentStatus.FAILED.getDescription());

		}

		// Set payload
		respo.setRawPayload(payload);

		return respo;

	}

}
