package com.practice.interview.poc;

import com.practice.interview.poc.entity.Account;
import com.practice.interview.poc.entity.repo.AccountRepository;
import com.practice.interview.poc.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
class IsolationTests {

    @Autowired
    private AccountService service;

    @Autowired
    private AccountRepository repo;

    @BeforeEach
    void cleanup() {
        repo.deleteAll();
    }

    ExecutorService executor = Executors.newFixedThreadPool(3);

    @Test
    void testReadCommitted() throws Exception {

        Account acc = repo.save(new Account(100));

        Future<Integer> firstRead = executor.submit(() -> service.readBalance(acc.getId()));

        service.updateBalance(acc.getId(), 500);

        Future<Integer> secondRead = executor.submit(() -> service.readBalance(acc.getId()));

        int first = firstRead.get();
        int second = secondRead.get();

        System.out.println(first + " -> " + second);

        assertNotEquals(first, second);
    }

    @Test
    void testRepeatableWithDelay() throws Exception {

        Account acc = repo.save(new Account(100));

        Future<int[]> result = executor.submit(() -> service.repeatableRead(acc.getId()));
        Thread.sleep(1000);

        System.out.println(Thread.currentThread().getName() +" is updating the balance");
        service.updateBalance(acc.getId(), 500);

        int[] values = result.get();
        System.out.println(values[0] + " -> " + values[1]);
        assertEquals(values[0], values[1]);
    }

    @Test
    void testReadCommittedWithWorkers() throws Exception {

        Account acc = repo.save(new Account(100));

        CountDownLatch firstReadDone = new CountDownLatch(1);
        CountDownLatch updateDone = new CountDownLatch(1);

        AtomicInteger firstValue = new AtomicInteger();
        AtomicInteger secondValue = new AtomicInteger();

        // THREAD 1 → first read
        Future<?> t1 = executor.submit(() -> {
            int val = service.readBalance(acc.getId());
            firstValue.set(val);
            System.out.println("First read: " + val);
            firstReadDone.countDown();
        });

        // THREAD 2 → update
        Future<?> t2 = executor.submit(() -> {
            try {
                firstReadDone.await();
                service.updateBalance(acc.getId(), 500);
                System.out.println("Balance updated to 500");
                updateDone.countDown();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        // THREAD 3 → second read
        Future<?> t3 = executor.submit(() -> {
            try {
                updateDone.await();
                int val = service.readBalance(acc.getId());
                secondValue.set(val);
                System.out.println("Second read: " + val);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        t1.get();
        t2.get();
        t3.get();

        int first = firstValue.get();
        int second = secondValue.get();

        System.out.println(first + " -> " + second);

        assertNotEquals(first, second);
    }

    @Test
    void repeatableReadTest() throws Exception {

        Account acc = repo.save(new Account(100));

        CountDownLatch firstReadDone = new CountDownLatch(1);
        CountDownLatch updateDone = new CountDownLatch(1);

        Future<int[]> t1 = executor.submit(() ->{
                    System.out.println(Thread.currentThread().getName() + " goes now...");
                    return service.repeatableRead(acc.getId(), firstReadDone, updateDone);
                }
        );

        Future<?> t2 = executor.submit(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + " waiting on firstReadDone");
                firstReadDone.await(); // make T2 wait.
                service.updateBalance(acc.getId(), 500);
                System.out.println("Balance updated to 500");
                updateDone.countDown(); //release T1
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        t2.get();
        int[] result = t1.get();

        System.out.println(result[0] + " -> " + result[1]);
        assertEquals(result[0], result[1]);
    }
}