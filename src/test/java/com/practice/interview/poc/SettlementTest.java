package com.practice.interview.poc;

import com.practice.interview.poc.entity.Payment;
import com.practice.interview.poc.entity.repo.PaymentRepository;
import com.practice.interview.poc.service.SettlementBatchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class SettlementTest {

    @Autowired
    SettlementBatchService settlementBatchService;

    @Autowired
    PaymentRepository paymentRepository;

    @BeforeEach
    void cleanup() {
        paymentRepository.deleteAll();
    }

    @Test
    void testBatchProcessing() {

        // Arrange (create test data)
        Payment p1 =   paymentRepository.save(new Payment(null,"ACC1", 5000.0, "PENDING"));
        Payment p2 =  paymentRepository.save(new Payment(null,"ACC2", 15000.0, "PENDING")); // will fail
        Payment p3 =  paymentRepository.save(new Payment(null,"ACC3", 2000.0, "PENDING"));

        // Act
        settlementBatchService.runSettlementBatch();

        // Assert
        Payment failedPayment = paymentRepository.findById(p2.getId()).orElseThrow();
        Payment successfulPayment = paymentRepository.findById(p3.getId()).orElseThrow();

        assertEquals("FAILED", failedPayment.getStatus());
        assertEquals("SUCCESS", successfulPayment.getStatus());
    }
}