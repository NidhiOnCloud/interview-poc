package com.practice.interview.poc.entity.repo;

import com.practice.interview.poc.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}