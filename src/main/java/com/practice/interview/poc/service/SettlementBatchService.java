package com.practice.interview.poc.service;

import com.practice.interview.poc.entity.Payment;
import com.practice.interview.poc.entity.repo.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SettlementBatchService {

    @Autowired
    private PaymentProcessorService processor;

    @Autowired
    private PaymentRepository paymentRepository;

    @Transactional
    public void runSettlementBatch() {

        List<Payment> payments = paymentRepository.findAll();
        for (Payment payment : payments) {
            try {
                processor.processPayment(payment);
            } catch (Exception e) {
                System.out.println("Payment failed: " + payment.getId());
                payment.setStatus("FAILED");
                paymentRepository.save(payment);
            }
        }
    }
}