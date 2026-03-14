package com.practice.interview.poc.entity.repo;

import com.practice.interview.poc.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
    long countByBalanceGreaterThan(int amount);
}