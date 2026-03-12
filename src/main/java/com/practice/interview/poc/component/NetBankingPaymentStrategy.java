package com.practice.interview.poc.component;

import com.practice.interview.poc.dto.PaymentResponse;
import org.springframework.stereotype.Component;

@Component("NET_BANKING")
public class NetBankingPaymentStrategy implements PaymentStrategy {

    @Override
    public PaymentResponse process(double amount, String userEmail) {
        System.out.println("Processing NetBanking...");
        return new PaymentResponse("SUCCESS", "Net Banking Payment Processed");
    }
}