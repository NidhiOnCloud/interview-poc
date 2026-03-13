package com.practice.interview.poc;

import com.practice.interview.poc.entity.Account;
import com.practice.interview.poc.entity.repo.AccountRepository;
import com.practice.interview.poc.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
class IsolationTests {

    @Autowired
    private AccountService service;

    @Autowired
    private AccountRepository repo;

    ExecutorService executor = Executors.newFixedThreadPool(2);

    @Test
    void readCommitted_allowsNonRepeatableRead() throws Exception {

        Account acc = repo.save(new Account(100));

        Future<Integer> firstRead =
                executor.submit(() -> service.readBalance(acc.getId()));

       // Thread.sleep(1000);

        service.updateBalance(acc.getId(), 500);

        Future<Integer> secondRead =
                executor.submit(() -> service.readBalance(acc.getId()));

        int first = firstRead.get();
        int second = secondRead.get();

        System.out.println(first + " -> " + second);

        assertNotEquals(first, second);
    }

    @Test
    void repeatableRead_preventsNonRepeatableRead() throws Exception {

        Account acc = repo.save(new Account(100));

        Future<int[]> result =
                executor.submit(() -> service.repeatableRead(acc.getId()));

        Thread.sleep(1000);

        service.updateBalance(acc.getId(), 500);

        int[] values = result.get();

        System.out.println(values[0] + " -> " + values[1]);

        assertEquals(values[0], values[1]);
    }
}