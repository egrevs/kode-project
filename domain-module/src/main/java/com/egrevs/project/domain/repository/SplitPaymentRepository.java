package com.egrevs.project.domain.repository;

import com.egrevs.project.domain.entity.payment.SplitPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SplitPaymentRepository extends JpaRepository<SplitPayment, String> {
}
