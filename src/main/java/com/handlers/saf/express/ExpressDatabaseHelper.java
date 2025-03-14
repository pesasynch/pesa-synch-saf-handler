package com.handlers.saf.express;

import java.math.BigDecimal;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.handlers.saf.entities.ExpressPayments;
import com.handlers.saf.express.models.ExpressResponseObject;
import com.handlers.saf.express.models.ExpressResultsObject;
import com.handlers.saf.models.PaymentRequestObject;
import com.handlers.saf.repsitories.ExpressPaymentsRepository;
import com.handlers.saf.utilities.PaymentStatus;

@Component
public class ExpressDatabaseHelper {

	@Autowired
	ExpressPaymentsRepository expressPaymentsRepository;

	public ExpressPayments insertPayment(PaymentRequestObject request) {

		ExpressPayments express = new ExpressPayments();
		express.setPaymentUuid(request.getPaymentUuid());
		express.setClientReference(request.getReference());
		express.setCreatedOn(Instant.now());
		express.setSourceMSISDN(request.getSource());
		express.setDestinationPaybill(request.getDestination());
		express.setCurrency(request.getCurrency());
		express.setAccountReference(request.getDescription());
		express.setAmount(BigDecimal.valueOf(request.getAmount()));
		express.setChargeAmount(BigDecimal.valueOf(request.getAmount()));
		express.setTotalAmount(express.getAmount().add(express.getChargeAmount()));
		express.setPaymentStatus(PaymentStatus.CREATED.getCode());
		express.setPaymentStatusDesc(PaymentStatus.CREATED.getDescription());

		return expressPaymentsRepository.save(express);

	}

	public void updateResponse(ExpressPayments express, ExpressResponseObject response) {

		express.setMnoAckId(response.getCheckoutRequestID());
		express.setMnoResponseCode(response.getResponseCode());
		express.setMnoResponseDesc(response.getResponseDescription());
		express.setMnoResponseText(response.getRawPayload());

		express.setPaymentStatus(response.getPaymentStatusCode());
		express.setPaymentStatusDesc(response.getPaymentStatusDesc());

		express.setMnoResponseSentOn(Instant.now());

		expressPaymentsRepository.save(express);
	}

	public void updateResults(ExpressResultsObject result) {

		ExpressPayments express = expressPaymentsRepository.findByMnoAckId(result.getCheckoutRequestID());
		if (express != null) {
			express.setMnoResultCode(result.getResultCode());
			express.setMnoResultDesc(result.getResultDesc());
			express.setMnoResultText(result.getRawPayload());

			express.setMnoResultsSentOn(Instant.now());
			express.setMnoReference(result.getMnoReference());

			express.setPaymentStatus(result.getPaymentStatusCode());
			express.setPaymentStatusDesc(result.getPaymentStatusDesc());

			expressPaymentsRepository.save(express);
		}

	}

}
