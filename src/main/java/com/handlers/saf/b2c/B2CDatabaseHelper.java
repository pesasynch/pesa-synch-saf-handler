package com.handlers.saf.b2c;

import java.math.BigDecimal;
import java.time.Instant;

import org.springframework.stereotype.Service;

import com.handlers.saf.b2c.models.B2CResponseObject;
import com.handlers.saf.b2c.models.B2CResultsObject;
import com.handlers.saf.entities.B2CPayments;
import com.handlers.saf.models.PaymentRequestObject;
import com.handlers.saf.repsitories.B2CPaymentsRepository;
import com.handlers.saf.utilities.PaymentStatus;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class B2CDatabaseHelper {

	final B2CPaymentsRepository b2cPaymentsRepository;

	public B2CPayments insertPayment(PaymentRequestObject request) {

		B2CPayments b2c = new B2CPayments();
		b2c.setPaymentUuid(request.getPaymentUuid());
		b2c.setClientReference(request.getReference());
		b2c.setCreatedOn(Instant.now());
		b2c.setSourceMsisdn(request.getSource());
		b2c.setDestinationPaybill(request.getDestination());
		b2c.setCurrency(request.getCurrency());
		b2c.setAccountNumber(request.getReference());
		b2c.setAmount(BigDecimal.valueOf(request.getAmount()));
		b2c.setChargeAmount(BigDecimal.valueOf(request.getAmount()));
		b2c.setTotalAmount(b2c.getAmount().add(b2c.getChargeAmount()));
		
		b2c.setPaymentStatus(PaymentStatus.CREATED.getCode());
		b2c.setPaymentStatusDesc(PaymentStatus.CREATED.getDescription());

		return b2cPaymentsRepository.save(b2c);

	}

	public void updateResponse(B2CPayments b2c, B2CResponseObject response) {

		b2c.setMnoAckId(response.getConversationID());
		b2c.setMnoResponseCode(response.getResponseCode());
		b2c.setMnoResponseDesc(response.getResponseDescription());
		b2c.setMnoResponseText(response.getRawPayload());

		b2c.setPaymentStatus(response.getPaymentStatusCode());
		b2c.setPaymentStatusDesc(response.getPaymentStatusDesc());

		b2c.setMnoResponseSentOn(Instant.now());

		b2cPaymentsRepository.save(b2c);
	}
	
	
	public void updateResults(B2CResultsObject result) {

		B2CPayments b2c = b2cPaymentsRepository.findByMnoAckId(result.getConversationId());
		if (b2c != null) {
			b2c.setMnoResultCode(result.getResultCode());
			b2c.setMnoResultDesc(result.getResultDesc());
			b2c.setMnoResultText(result.getRawPayload());
			b2c.setMnoResultsSentOn(Instant.now());

			b2c.setMnoReference(result.getMnoReference());

			b2c.setReceiverName(result.getReceiverName());
			b2c.setAccountBalance(BigDecimal.valueOf(result.getAccountBalance()));
			
			b2c.setPaymentStatus(result.getPaymentStatusCode());
			b2c.setPaymentStatusDesc(result.getPaymentStatusDesc());

			b2cPaymentsRepository.save(b2c);
		}

	}


}
