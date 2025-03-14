package com.handlers.saf.repsitories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.handlers.saf.entities.B2BPayments;

public interface B2BPaymentsRepository extends JpaRepository<B2BPayments, Long> {

	B2BPayments findByMnoAckId(String mnoAckId);

}
