package com.practice.interview.poc.component;

import com.practice.interview.poc.dto.PaymentResponse;
import org.springframework.stereotype.Component;

@Component("CREDIT_CARD")
public class CreditCardPaymentStrategy implements PaymentStrategy {

    @Override
    public PaymentResponse process(double amount, String userEmail) {
        System.out.println("Calling Visa API...");
        return new PaymentResponse("SUCCESS", "Credit Card Payment Processed");
    }
}