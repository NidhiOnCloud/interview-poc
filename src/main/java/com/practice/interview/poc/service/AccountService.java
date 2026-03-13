package com.practice.interview.poc.service;

import com.practice.interview.poc.entity.Account;
import com.practice.interview.poc.entity.repo.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import java.util.concurrent.CountDownLatch;

@Service
public class AccountService {

    private final AccountRepository repo;

    public AccountService(AccountRepository repo) {
        this.repo = repo;
    }

    /**
     * READ_COMMITTED → always latest committed value committed by other transaction.
     * A transaction can only read(see) committed data as latest from other transactions.
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public int readBalance(Long id) {
        return repo.findById(id).orElseThrow().getBalance();
    }

    /**
     * REPEATABLE_READ guarantees that once a transaction reads a row, it will always see the same value for that row during the entire transaction,
     * even if another transaction updates and commits that row meanwhile.
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public int[] repeatableRead(Long id) throws InterruptedException {
        System.out.println("Reading repeated...");
        int first = repo.findById(id).orElseThrow().getBalance();

        System.out.println(Thread.currentThread().getName() +" is sleeping for 3 seconds");
        Thread.sleep(3000);

        System.out.println(Thread.currentThread().getName() +" is in-charge now");
        int second = repo.findById(id).orElseThrow().getBalance();

        return new int[]{first, second};
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public int[] repeatableRead(Long id,
                                CountDownLatch firstReadDone,
                                CountDownLatch updateDone) throws InterruptedException {

        System.out.println("Thread ----->>> "+Thread.currentThread().getName()+" : reading balance for the account "+id);
        int first = repo.findById(id).orElseThrow().getBalance();

        System.out.println(Thread.currentThread().getName() + " releasing firstReadDone");
        firstReadDone.countDown(); // releases T2

        System.out.println(Thread.currentThread().getName() + " waiting on updateDone");
        updateDone.await(); // make T1 wait
        System.out.println("Thread ----->>> "+Thread.currentThread().getName()+" : reading balance again for the account "+id);

        int second = repo.findById(id).orElseThrow().getBalance();
        return new int[]{first, second};
    }

    @Transactional
    public void updateBalance(Long id, int newBalance) {
        Account acc = repo.findById(id).orElseThrow();
        System.out.println("Thread ----->>> "+Thread.currentThread().getName()+" : updating balance for the account "+acc.getId());
        acc.setBalance(newBalance);
       // printTxInfo();
    }
    private void printTxInfo() {
        System.out.println("Tx active: " +
                TransactionSynchronizationManager.isActualTransactionActive());

        System.out.println("Tx name: " +
                TransactionSynchronizationManager.getCurrentTransactionName());

        System.out.println("Tx readOnly: " +
                TransactionSynchronizationManager.isCurrentTransactionReadOnly());
    }
}