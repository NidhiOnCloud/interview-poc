package com.practice.interview.poc.service;

import com.practice.interview.poc.entity.OrderEvent;
import com.practice.interview.poc.entity.repo.OrderEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {

    @Autowired
    private OrderEventRepository repo;

    @Transactional(propagation = Propagation.REQUIRED) // joins placeOrder Txn
    public void chargeWhenNoPaymentGatewayProblem() {
        System.out.println("**************** UP : Payment Gateway is UP  ********************");
        repo.save(new OrderEvent("Payment Charged"));
    }
    @Transactional(propagation = Propagation.REQUIRED) // joins placeOrder Txn
    public void chargePayment() {
        repo.save(new OrderEvent("Payment Charged"));
        System.out.println("**************** DOWN : Payment Gateway is DOWN ********************");
        throw new RuntimeException("Payment Gateway Down");
    }
}