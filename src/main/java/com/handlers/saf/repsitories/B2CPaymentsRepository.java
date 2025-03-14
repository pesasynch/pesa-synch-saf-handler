package com.handlers.saf.repsitories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.handlers.saf.entities.B2CPayments;

public interface B2CPaymentsRepository extends JpaRepository<B2CPayments, Long> {
	
	B2CPayments findByMnoAckId(String mnoAckId);

}
