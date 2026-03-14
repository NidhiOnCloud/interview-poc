package com.practice.interview.poc;

import com.practice.interview.poc.entity.Account;
import com.practice.interview.poc.entity.repo.AccountRepository;
import com.practice.interview.poc.service.SerializableService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class SerializableIsolationTest {

    @Autowired
    SerializableService service;

    @Autowired
    AccountRepository repo;

    ExecutorService executor = Executors.newFixedThreadPool(2);

    @Test
    void serializable_preventsPhantomRead() throws Exception {

        repo.save(new Account(200));

        CountDownLatch firstQueryDone = new CountDownLatch(1);
        CountDownLatch insertDone = new CountDownLatch(1);

        Future<Long> t1 = executor.submit(() ->
            service.countRichAccounts(
                100,
                firstQueryDone,
                insertDone
            )
        );

        executor.submit(() -> {

            firstQueryDone.await();

            service.insertAccount(300);

            insertDone.countDown();

            return null;
        });

        long result = t1.get();

        assertEquals(1, result);
    }
}