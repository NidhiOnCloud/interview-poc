package com.practice.interview.poc.service;

import com.practice.interview.poc.entity.Account;
import com.practice.interview.poc.entity.repo.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OuterService {

    @Autowired
    private AccountRepository repo;

    @Autowired
    private InnerService innerService;

    @Transactional
    public void outerRequired() {

        repo.save(new Account("outer"));

        try {
            innerService.requiredMethod("inner-required");
        } catch (Exception e) {
        }
    }

    @Transactional
    public void outerRequiresNew() {

        repo.save(new Account("outer"));

        try {
            innerService.requiresNewMethod("inner-new");
        } catch (Exception e) {
        }
    }
}