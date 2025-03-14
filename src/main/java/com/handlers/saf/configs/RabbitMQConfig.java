package com.handlers.saf.configs;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

	String EXCHANGE = "SAF.HANDLER.EXCHANGE";

	String EXPRESS_QUEUE = "SAF.HANDLER.QUEUE.EXPRESS";

	String B2B_QUEUE = "SAF.HANDLER.QUEUE.B2B";

	String B2C_QUEUE = "SAF.HANDLER.QUEUE.B2C";
	
	String DLQ_QUEUE = "SAF.HANDLER.DLQ";

	
	@Bean
	public Queue expressQueue() {
		return QueueBuilder.durable(EXPRESS_QUEUE).deadLetterExchange(EXCHANGE).deadLetterRoutingKey(DLQ_QUEUE)
				.build();

	}

	@Bean
	public Queue b2bQueue() {
		return QueueBuilder.durable(B2B_QUEUE).deadLetterExchange(EXCHANGE).deadLetterRoutingKey(DLQ_QUEUE).build();

	}
	
	@Bean
	public Queue b2cQueue() {
		return QueueBuilder.durable(B2C_QUEUE).deadLetterExchange(EXCHANGE).deadLetterRoutingKey(DLQ_QUEUE).build();

	}
	
	
	@Bean
	public Queue dlqQueue() {
		return QueueBuilder.durable(DLQ_QUEUE).deadLetterExchange(EXCHANGE).build();

	}

	@Bean
	public Binding bindingExpress(Queue expressQueue, DirectExchange exchange) {
		return BindingBuilder.bind(expressQueue).to(exchange).with(EXPRESS_QUEUE);
	}

	@Bean
	public Binding bindingB2B(Queue b2bQueue, DirectExchange exchange) {
		return BindingBuilder.bind(b2bQueue).to(exchange).with(B2B_QUEUE);
	}
	
	
	@Bean
	public Binding bindingB2C(Queue b2cQueue, DirectExchange exchange) {
		return BindingBuilder.bind(b2cQueue).to(exchange).with(B2C_QUEUE);
	}
	
	
	@Bean
	public Binding bindingDLQ(Queue dlqQueue, DirectExchange exchange) {
		return BindingBuilder.bind(dlqQueue).to(exchange).with(DLQ_QUEUE);
	}

	@Bean
	public DirectExchange exchange() {
		return new DirectExchange(EXCHANGE);
	}

	@Bean
	public MessageConverter messageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(messageConverter());
		return rabbitTemplate;
	}

}
