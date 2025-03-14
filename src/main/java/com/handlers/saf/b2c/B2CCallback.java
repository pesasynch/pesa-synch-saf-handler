package com.handlers.saf.b2c;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.handlers.saf.b2c.models.B2CResultsObject;
import com.handlers.saf.exceptions.DuplicateCallbackException;
import com.handlers.saf.global.Callbacks;
import com.handlers.saf.models.SafResponseObject;
import com.handlers.saf.utilities.PaymentStatus;
import com.handlers.saf.utilities.Utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/safaricom/b2c")
public class B2CCallback extends Callbacks {

	@Autowired
	B2CDatabaseHelper databaseHelper;

	@PostMapping("/result")
	public ResponseEntity<SafResponseObject> processCallbacks(@RequestBody String payload) {

		log.info("Received b2c result: {}", payload);
		try {
			// Process results
			databaseHelper.updateResults(processResults(payload));

		} catch (DuplicateCallbackException e) {
			log.info("Duplicate callback: {}", e.getMessage());
		}

		return ResponseEntity.status(200).body(new SafResponseObject());
	}

	@PostMapping("/timeout")
	public ResponseEntity<SafResponseObject> processTimeouts(@RequestBody String payload) {

		return ResponseEntity.status(200).body(new SafResponseObject());
	}

	private B2CResultsObject processResults(String payload) throws DuplicateCallbackException {

		JSONObject results = new JSONObject(payload).getJSONObject("Result");

		B2CResultsObject respo = new B2CResultsObject();
		respo.setResultCode(results.optString("ResultCode"));
		respo.setResultDesc(results.optString("ResultDesc"));
		respo.setOriginatorConversationID(results.optString("OriginatorConversationID"));
		respo.setConversationId(results.optString("ConversationID"));
		respo.setTransactionId(results.optString("TransactionID"));

		duplicatesChecker(respo.getConversationId());

		respo.setRawPayload(payload);

		if ("0".equalsIgnoreCase(respo.getResultCode())) {

			var resultParams = Utils.extractMetadata(results.get("ResultParameters").toString(), "ResultParameter");

			respo.setReceiverName(resultParams.getOrDefault("ReceiverPartyPublicName", ""));

			respo.setPaymentStatusCode(PaymentStatus.SUCCESS.getCode());
			respo.setPaymentStatusDesc(PaymentStatus.SUCCESS.getDescription());

		} else {

			respo.setPaymentStatusCode(PaymentStatus.FAILED.getCode());
			respo.setPaymentStatusDesc(PaymentStatus.FAILED.getDescription());
		}

		return respo;

	}

}
