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
public class AccountServiceAgain {

    private final AccountRepository repository;

    @PersistenceContext
    private final EntityManager entityManager;

    AccountServiceAgain(AccountRepository repo,EntityManager entityManager){
        repository = repo;
        this.entityManager = entityManager;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public int[] readCommittedRepeatable(CountDownLatch read, CountDownLatch update, Long id) throws InterruptedException {
        int readFirst = repository.findById(id).orElseThrow().getBalance();
        read.countDown(); // releases writer.
        update.await(); // awaits for the update.
        entityManager.clear();
        int secondRead = repository.findById(id).orElseThrow().getBalance();
        return new int[]{readFirst,secondRead};
    }

    @Transactional
    public void updateBalance(Long id, int newBalance){
        Account account = repository.findById(id).orElseThrow();
        account.setBalance(account.getBalance()+newBalance);
        /*
         * No save() needed — JPA dirty checking persists automatically.
         */
       // repository.save(account);
    }
}
