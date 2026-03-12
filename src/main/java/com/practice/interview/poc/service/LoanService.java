package com.practice.interview.poc.service;

import com.practice.interview.poc.entity.LoanEvent;
import com.practice.interview.poc.entity.repo.LoanEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoanService {

    @Autowired
    private LoanEventRepository repo;

    @Autowired
    private AnalyticsService analytics;

    @Transactional
    public void applyLoan() {
        repo.save(new LoanEvent("Loan request created"));
        analytics.publishEvent();
        throw new RuntimeException("Loan processing failure"); // Because an exception escaped the transactional method,
                                                                // no UnexpectedRollbackException silently thrown by spring.
    }

    @Transactional
    public void applyLoanWithFailureInAnalytics() {
        repo.save(new LoanEvent("Loan request created"));
        analytics.publishEventWithAnException();
        throw new RuntimeException("Loan processing failure"); // Because an exception escaped the transactional method,
                                                                // no UnexpectedRollbackException silently thrown by spring.
    }
}