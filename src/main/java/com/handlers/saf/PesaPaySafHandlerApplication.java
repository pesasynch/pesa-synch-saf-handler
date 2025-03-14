package com.handlers.saf;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@EnableRabbit
@SpringBootApplication
public class PesaPaySafHandlerApplication {

	public static void main(String[] args) {
		SpringApplication.run(PesaPaySafHandlerApplication.class, args);
	}

}
