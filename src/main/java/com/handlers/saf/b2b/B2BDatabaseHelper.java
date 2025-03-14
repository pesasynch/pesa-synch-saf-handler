package com.handlers.saf.b2b;

import java.math.BigDecimal;
import java.time.Instant;

import org.springframework.stereotype.Service;

import com.handlers.saf.b2b.models.B2BResponseObject;
import com.handlers.saf.b2b.models.B2BResultsObject;
import com.handlers.saf.entities.B2BPayments;
import com.handlers.saf.models.PaymentRequestObject;
import com.handlers.saf.repsitories.B2BPaymentsRepository;
import com.handlers.saf.utilities.PaymentStatus;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class B2BDatabaseHelper {

	final B2BPaymentsRepository b2bPaymentsRepository;

	public B2BPayments insertPayment(PaymentRequestObject request) {

		B2BPayments b2b = new B2BPayments();
		b2b.setPaymentUuid(request.getPaymentUuid());
		b2b.setClientReference(request.getReference());
		b2b.setCreatedOn(Instant.now());
		b2b.setSourcePaybill(request.getSource());
		b2b.setDestinationPaybill(request.getDestination());
		b2b.setCurrency(request.getCurrency());
		b2b.setAccountNumber(request.getReference());
		b2b.setAmount(BigDecimal.valueOf(request.getAmount()));
		b2b.setChargeAmount(BigDecimal.valueOf(request.getAmount()));
		b2b.setTotalAmount(b2b.getAmount().add(b2b.getChargeAmount()));
		b2b.setPaymentStatus(PaymentStatus.CREATED.getCode());
		b2b.setPaymentStatusDesc(PaymentStatus.CREATED.getDescription());

		return b2bPaymentsRepository.save(b2b);

	}

	public void updateResponse(B2BPayments b2b, B2BResponseObject response) {

		b2b.setMnoAckId(response.getConversationID());
		b2b.setMnoResponseCode(response.getResponseCode());
		b2b.setMnoResponseDesc(response.getResponseDescription());
		b2b.setMnoResponseText(response.getRawPayload());

		b2b.setPaymentStatus(response.getPaymentStatusCode());
		b2b.setPaymentStatusDesc(response.getPaymentStatusDesc());

		b2b.setMnoResponseSentOn(Instant.now());

		b2bPaymentsRepository.save(b2b);
	}

	public void updateResults(B2BResultsObject result) {

		B2BPayments b2b = b2bPaymentsRepository.findByMnoAckId(result.getConversationId());
		if (b2b != null) {
			b2b.setMnoResultCode(result.getResultCode());
			b2b.setMnoResultDesc(result.getResultDesc());
			b2b.setMnoResultText(result.getRawPayload());
			b2b.setMnoResultsSentOn(Instant.now());

			b2b.setMnoReference(result.getMnoReference());

			b2b.setReceiverName(result.getReceiverName());
			b2b.setAccountBalance(BigDecimal.valueOf(result.getAccountBalance()));
			
			
			b2b.setPaymentStatus(result.getPaymentStatusCode());
			b2b.setPaymentStatusDesc(result.getPaymentStatusDesc());
			
			

			b2bPaymentsRepository.save(b2b);
		}

	}

}
