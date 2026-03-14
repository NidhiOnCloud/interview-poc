package com.practice.interview.poc;

import com.practice.interview.poc.entity.Account;
import com.practice.interview.poc.entity.repo.AccountRepository;
import com.practice.interview.poc.service.AccountServiceAgain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
class IsolationTestHandsOn {

    @Autowired
    AccountRepository repository;

    @Autowired
    AccountServiceAgain accountAgainService;

    ExecutorService executorService = Executors.newFixedThreadPool(2);

    CountDownLatch readLatch  = new CountDownLatch(1);
    CountDownLatch updateLatch = new CountDownLatch(1);

    @BeforeEach
    void cleanUp(){repository.deleteAll();}

    @Test
    void testRepeatedRead() throws Exception{
        Account account = new Account(100);
        repository.save(account);
        Future<int[]> readWorker = executorService.submit(() ->
                        accountAgainService.readCommittedRepeatable(readLatch,updateLatch,account.getId()));
        Future<?> updateWorker = executorService.submit(()->{
            try {
                readLatch.await(); //updater waits for the reader.
                accountAgainService.updateBalance(account.getId(),500);
                updateLatch.countDown(); // gives change to the reader.
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        int [] result = readWorker.get();
        updateWorker.get();
        assertNotEquals(result[0],result[1]);
    }
}
