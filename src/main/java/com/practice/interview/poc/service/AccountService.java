package com.practice.interview.poc.service;

import com.practice.interview.poc.entity.Account;
import com.practice.interview.poc.entity.repo.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountService {

    private final AccountRepository repo;

    public AccountService(AccountRepository repo) {
        this.repo = repo;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public int readBalance(Long id) {
        return repo.findById(id).orElseThrow().getBalance();
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public int[] repeatableRead(Long id) throws InterruptedException {
        System.out.println("Reading repeated...");
        int first = repo.findById(id).orElseThrow().getBalance();

        Thread.sleep(3000);

        int second = repo.findById(id).orElseThrow().getBalance();

        return new int[]{first, second};
    }

    @Transactional
    public void updateBalance(Long id, int newBalance) {
        Account acc = repo.findById(id).orElseThrow();
        acc.setBalance(newBalance);
    }
}