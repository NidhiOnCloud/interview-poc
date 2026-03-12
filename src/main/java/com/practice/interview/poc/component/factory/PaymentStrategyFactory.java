package com.practice.interview.poc.component.factory;

import com.practice.interview.poc.component.PaymentStrategy;
import com.practice.interview.poc.dto.PaymentType;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PaymentStrategyFactory {

    private final Map<String, PaymentStrategy> strategyMap;

    public PaymentStrategyFactory(Map<String, PaymentStrategy> strategyMap) {
        this.strategyMap = strategyMap;
    }

    public PaymentStrategy getStrategy(PaymentType type) {
        return strategyMap.get(type.name());
    }
}