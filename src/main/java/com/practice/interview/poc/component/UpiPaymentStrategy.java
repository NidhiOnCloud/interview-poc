package com.practice.interview.poc.component;

import com.practice.interview.poc.dto.PaymentResponse;
import org.springframework.stereotype.Component;

@Component("UPI")
public class UpiPaymentStrategy implements PaymentStrategy {

    @Override
    public PaymentResponse process(double amount, String userEmail) {
        System.out.println("Processing UPI payment...");
        return new PaymentResponse("SUCCESS", "UPI Payment Processed");
    }
}