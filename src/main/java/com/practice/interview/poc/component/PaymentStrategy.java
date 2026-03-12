package com.practice.interview.poc.component;

import com.practice.interview.poc.dto.PaymentResponse;

public interface PaymentStrategy {
    PaymentResponse process(double amount, String userEmail);
}