package com.practice.interview.poc.service;

import com.practice.interview.poc.entity.Payment;
import com.practice.interview.poc.entity.repo.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentProcessorService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW) //NESTED : Spring JPA doesn't support NESTED
    public void processPayment(Payment payment) {

        System.out.println("Processing payment: " + payment.getId());

        if (payment.getAmount() > 10000) {
            throw new RuntimeException("Fraud detection triggered");
        }

        payment.setStatus("SUCCESS");
        paymentRepository.save(payment);
    }
}