package com.practice.interview.poc.service;

import com.practice.interview.poc.entity.LoanEvent;
import com.practice.interview.poc.entity.repo.LoanEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
public class AnalyticsService {

    @Autowired
    private LoanEventRepository repo;

    /**
     * Suspend any existing transaction
     * Run this method non-transactionally
     * So database behavior becomes: Auto-commit mode, whether this method throws an exception or not.
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void publishEvent() {
        repo.save(new LoanEvent("Analytics event stored"));
        System.out.println(
            "Transaction active: " +
            TransactionSynchronizationManager.isActualTransactionActive()
        );
    }

    /**
     * Suspend any existing transaction
     * Run this method non-transactionally
     * So database behavior becomes: Auto-commit mode, whether this method throws an exception or not.
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void publishEventWithAnException() {
        repo.save(new LoanEvent("Analytics event stored"));
        System.out.println(
                "Transaction active: " +
                        TransactionSynchronizationManager.isActualTransactionActive()
        );
        throw new RuntimeException("Exception inside Analytical service");
    }
}