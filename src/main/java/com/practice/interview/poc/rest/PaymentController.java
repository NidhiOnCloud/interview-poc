package com.practice.interview.poc.rest;

import com.practice.interview.poc.dto.PaymentRequest;
import com.practice.interview.poc.dto.PaymentResponse;
import com.practice.interview.poc.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

   /* @PostMapping
    public ResponseEntity<PaymentResponse> processPayment(
            @RequestHeader("Idempotency-Key") String idempotencyKey,
            @Valid @RequestBody PaymentRequest request) {
        PaymentResponse response = paymentService.processPayment(request,idempotencyKey);
        return ResponseEntity.ok(response);
    }*/
}