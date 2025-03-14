package com.handlers.saf.entries;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.handlers.saf.b2b.B2BRequest;
import com.handlers.saf.b2c.B2CRequest;
import com.handlers.saf.express.ExpressRequest;
import com.handlers.saf.models.PaymentRequestObject;
import com.handlers.saf.models.RestResponseObject;
import jakarta.annotation.PreDestroy;

@RestController
@RequestMapping("/safaricom/payments/v1")
public class RestEntry {

	@Autowired
	ExpressRequest expressRequest;

	@Autowired
	B2BRequest b2bRequest;

	@Autowired
	B2CRequest b2cRequest;

	private final ExecutorService executorService = Executors.newFixedThreadPool(10);

	@PostMapping("/express")
	public ResponseEntity<RestResponseObject> processExpress(@RequestBody PaymentRequestObject paymentRequest) {

		System.out.println("Received request : " + paymentRequest.toString());

		executorService.submit(() -> {
			expressRequest.init(paymentRequest);

		});

		return ResponseEntity.status(200).body(new RestResponseObject());
	}

	@PostMapping("/b2b")
	public ResponseEntity<RestResponseObject> processB2B(@RequestBody PaymentRequestObject paymentRequest) {

		System.out.println("Received requeest : " + paymentRequest.toString());

		executorService.submit(() -> {
			b2bRequest.init(paymentRequest);
		});
		return ResponseEntity.status(200).body(new RestResponseObject());
	}

	@PostMapping("/b2c")
	public ResponseEntity<RestResponseObject> processB2C(@RequestBody PaymentRequestObject paymentRequest) {

		System.out.println("Received request : " + paymentRequest.toString());

		executorService.submit(() -> {
			b2cRequest.init(paymentRequest);
		});

		return ResponseEntity.status(200).body(new RestResponseObject());
	}

	@PreDestroy
	public void shutdown() {
		executorService.shutdown();
	}
}
