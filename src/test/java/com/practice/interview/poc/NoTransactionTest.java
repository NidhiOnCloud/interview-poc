package com.practice.interview.poc;

import com.practice.interview.poc.entity.LoanEvent;
import com.practice.interview.poc.entity.repo.LoanEventRepository;
import com.practice.interview.poc.service.LoanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class NoTransactionTest {

    @Autowired
    LoanService loanService;

    @Autowired
    LoanEventRepository repo;

    @BeforeEach
    void cleanup() {
        repo.deleteAll();
    }

    @Test
    void testMethodWithoutTransaction() {

        try {
            loanService.applyLoan();
        } catch (Exception ignored) {}

        List<LoanEvent> events = repo.findAll();

        assertEquals(1, events.size());
        assertEquals("Analytics event stored",
                events.get(0).getMessage()
        );
    }
    @Test
    void testMethodWithoutTransactionFailedAnalytics() {

        try {
            loanService.applyLoanWithFailureInAnalytics();
        } catch (Exception ignored) {}

        List<LoanEvent> events = repo.findAll();
        assertEquals(1, events.size());
        assertEquals("Analytics event stored", events.get(0).getMessage()
        );
    }
}