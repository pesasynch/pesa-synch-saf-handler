package com.handlers.saf.entries;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.handlers.saf.b2b.B2BRequest;
import com.handlers.saf.b2c.B2CRequest;
import com.handlers.saf.express.ExpressRequest;
import com.handlers.saf.models.PaymentRequestObject;

@Component
public class RabbitMQConsumerEntry {

	@Autowired
	ExpressRequest expressRequest;

	@Autowired
	B2BRequest b2bRequest;
	
	@Autowired
	B2CRequest b2cRequest;

	@RabbitListener(queues = "SAF.HANDLER.QUEUE.EXPRESS", concurrency = "25")
	public void consumeExpress(@Payload PaymentRequestObject paymentRequest) {

		expressRequest.init(paymentRequest);

	}

	@RabbitListener(queues = "SAF.HANDLER.QUEUE.B2B", concurrency = "25")
	public void consumeB2B(@Payload PaymentRequestObject paymentRequest) {

		b2bRequest.init(paymentRequest);

	}
	
	
	@RabbitListener(queues = "SAF.HANDLER.QUEUE.B2C", concurrency = "25")
	public void consumeB2C(@Payload PaymentRequestObject paymentRequest) {

		b2cRequest.init(paymentRequest);

	}

}
