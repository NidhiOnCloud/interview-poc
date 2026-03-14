package com.practice.interview.poc.service;

import com.practice.interview.poc.entity.Account;
import com.practice.interview.poc.entity.repo.AccountRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CountDownLatch;

@Service
public class SerializableService {

    private final AccountRepository repo;

    public SerializableService(AccountRepository repo) {
        this.repo = repo;
    }

    /**
     *
     * SERIALIZABLE guarantees that concurrent transactions produce the same result as if they were executed sequentially.
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public long countRichAccounts(
            int amount,
            CountDownLatch firstQueryDone,
            CountDownLatch insertDone) throws Exception {

        long first = repo.countByBalanceGreaterThan(amount);

        firstQueryDone.countDown();

        insertDone.await();

        long second = repo.countByBalanceGreaterThan(amount);

        return second;
    }

    @Transactional
    public void insertAccount(int balance) {
        repo.save(new Account(balance));
    }
}