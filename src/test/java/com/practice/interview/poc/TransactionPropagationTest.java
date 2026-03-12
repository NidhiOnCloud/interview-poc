package com.practice.interview.poc;

import com.practice.interview.poc.entity.OrderEvent;
import com.practice.interview.poc.entity.repo.OrderEventRepository;
import com.practice.interview.poc.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.UnexpectedRollbackException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class TransactionPropagationTest {

    @Autowired
    OrderService orderService;

    @Autowired
    OrderEventRepository repo;

    @BeforeEach
    void cleanup() {
        repo.deleteAll();
    }

    @Test
    void verifyOrderAndPaymentSuccess() {
        orderService.placeOrder();
        System.out.println("continue in verifyTransactionBehavior: ");
        List<OrderEvent> events = repo.findAll();
        System.out.println("Total events: " + events.size());
        assertEquals(2, events.size());
        assertEquals("Order Created", events.get(0).getMessage());
        assertEquals("Payment Charged", events.get(1).getMessage());
    }

    @Test
    void verifyOrderEventsWhenPaymentFailed() {

        assertThrows(UnexpectedRollbackException.class, () -> {
            orderService.placeOrderWhenPaymentGateWayIsDownWithAuditInNewTxn();
        });

        List<OrderEvent> events = repo.findAll();
        System.out.println("Total events: " + events.size());
        assertEquals(1, events.size());
        assertEquals("AUDIT: Payment failed", events.get(0).getMessage());
    }
    @Test
    void verifyOrderEventsWhenPaymentWithAuditInNewTx() {

        assertThrows(UnexpectedRollbackException.class, () -> {
            orderService.placeOrderWhenPaymentGateWayIsDownWithAuditInNewTxn();
        });

        List<OrderEvent> events = repo.findAll();
        assertEquals(1, events.size());
        assertEquals("AUDIT: Payment failed", events.get(0).getMessage());
    }
    @Test
    void verifyOrderEventsWhenPaymentWithAuditInSameTx() { //expected create order, payment charged and audit to be rolled back under the same transaction TX1.

        assertThrows(UnexpectedRollbackException.class, () -> {
            orderService.placeOrderWhenPaymentGateWayIsDownWithAuditInSameTxn();
        });
        List<OrderEvent> events = repo.findAll();
        assertEquals(0, events.size());
    }
}