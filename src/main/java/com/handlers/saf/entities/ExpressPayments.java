package com.handlers.saf.entities;

import java.math.BigDecimal;
import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "express_payments")
public class ExpressPayments {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "payment_uuid")
	private String paymentUuid;

	@Column(name = "client_reference", nullable = false)
	private String clientReference;

	@Column(name = "created_on", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Instant createdOn;

	@Column(name = "destination_paybill")
	private String destinationPaybill;

	@Column(name = "source_msisdn")
	private String sourceMSISDN;

	@Column(name = "currency", length = 3, columnDefinition = "VARCHAR(3) DEFAULT 'KES'")
	private String currency = "KES";

	@Column(name = "account_reference")
	private String accountReference;

	@Column(name = "amount", precision = 18, scale = 2)
	private BigDecimal amount;

	@Column(name = "charge_amount", precision = 18, scale = 2)
	private BigDecimal chargeAmount;

	@Column(name = "total_amount", precision = 18, scale = 2)
	private BigDecimal totalAmount;

	@Column(name = "retries", columnDefinition = "INT DEFAULT 0")
	private Integer retries = 0;

	@Column(name = "retry_reasons")
	private String retryReasons;

	@Column(name = "payment_status", columnDefinition = "INT DEFAULT 0")
	private Integer paymentStatus = 0;

	@Column(name = "payment_status_desc")
	private String paymentStatusDesc;

	@Column(name = "mno_ack_id")
	private String mnoAckId;

	@Column(name = "mno_response_code")
	private String mnoResponseCode;

	@Column(name = "mno_response_desc")
	private String mnoResponseDesc;

	@Column(name = "mno_response_sent_on")
	private Instant mnoResponseSentOn;

	@Column(name = "mno_response_text", columnDefinition = "TEXT")
	private String mnoResponseText;

	@Column(name = "mno_result_code")
	private String mnoResultCode;

	@Column(name = "mno_result_desc")
	private String mnoResultDesc;

	@Column(name = "mno_result_text", columnDefinition = "TEXT")
	private String mnoResultText;

	@Column(name = "mno_results_sent_on")
	private Instant mnoResultsSentOn;

	@Column(name = "mno_reference")
	private String mnoReference;

}
