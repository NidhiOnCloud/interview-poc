package com.practice.interview.poc.service;

import com.practice.interview.poc.entity.OrderEvent;
import com.practice.interview.poc.entity.repo.OrderEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditService {

    @Autowired
    private OrderEventRepository repo;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void log(String msg) {
        System.out.println("AUDIT: "+msg);
        repo.save(new OrderEvent("AUDIT: " + msg));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void logSameTxn(String msg) {
        System.out.println("AUDIT: "+msg);
        repo.save(new OrderEvent("AUDIT: " + msg));
    }
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logFailed(String msg) {
        System.out.println("AUDIT: "+msg);
        repo.save(new OrderEvent("AUDIT: " + msg));
        throw new RuntimeException("AUDIT: Failed due to runtime issue");
    }
}