package com.practice.interview.poc.service;

import com.practice.interview.poc.entity.Account;
import com.practice.interview.poc.entity.repo.AccountRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InnerService {

    @Autowired
    private AccountRepository repo;

    @Transactional(propagation = Propagation.REQUIRED)
    public void requiredMethod(String name) {
        repo.save(new Account(name));
        throw new RuntimeException("Failure in REQUIRED");
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void requiresNewMethod(String name) {
        repo.save(new Account(name));
        throw new RuntimeException("Failure in REQUIRES_NEW");
    }
}