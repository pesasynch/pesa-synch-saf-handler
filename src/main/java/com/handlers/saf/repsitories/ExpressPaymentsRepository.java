package com.handlers.saf.repsitories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.handlers.saf.entities.ExpressPayments;

public interface ExpressPaymentsRepository extends JpaRepository<ExpressPayments, Long> {

	ExpressPayments findByMnoAckId(String mnoAckId);

}
