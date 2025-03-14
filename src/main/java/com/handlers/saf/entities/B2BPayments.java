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
@Table(name = "b2b_payments")
public class B2BPayments {
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

	@Column(name = "source_paybill")
	private String sourcePaybill;

	@Column(name = "destination_paybill")
	private String destinationPaybill;

	@Column(name = "currency", length = 3, columnDefinition = "VARCHAR(3) DEFAULT 'KES'")
	private String currency = "KES";

	@Column(name = "account_number")
	private String accountNumber;

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
	
	@Column(name = "receiver_name")
	private String receiverName;

	@Column(name = "account_balance")
	private BigDecimal accountBalance;

	@Column(name = "reversed", columnDefinition = "TINYINT(1) DEFAULT 0")
	private Boolean reversed = false;

	@Column(name = "reversed_on")
	private Instant reversedOn;

	@Column(name = "reversal_reference")
	private String reversalReference;

	@Column(name = "reversal_status")
	private Integer reversalStatus;

	@Column(name = "reversal_desc")
	private String reversalDesc;
}
