package com.practice.interview.poc.entity.repo;

import com.practice.interview.poc.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboxRepository extends JpaRepository<Payment, Long> {
}