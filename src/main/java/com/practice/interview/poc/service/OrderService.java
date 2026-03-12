package com.practice.interview.poc.service;

import com.practice.interview.poc.entity.OrderEvent;
import com.practice.interview.poc.entity.repo.OrderEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    @Autowired
    private OrderEventRepository repo;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private AuditService auditService;

    @Transactional
    public void placeOrder() {
        repo.save(new OrderEvent("Order Created"));
        paymentService.chargeWhenNoPaymentGatewayProblem();
    }
    @Transactional
    public void placeOrderWhenPaymentGateWayIsDownWithAuditInNewTxn() {
        repo.save(new OrderEvent("Order Created"));
        try {
            paymentService.chargePayment();
        } catch (Exception e) {
            auditService.log("Payment failed");
        }
    }
    @Transactional
    public void placeOrderWhenPaymentGateWayIsDownWithAuditInSameTxn() {
        repo.save(new OrderEvent("Order Created"));
        try {
            paymentService.chargePayment();
        } catch (Exception e) {
            auditService.logSameTxn("Payment failed");
        }
    }
}