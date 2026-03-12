package com.practice.interview.poc.entity.repo;

import com.practice.interview.poc.entity.LoanEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanEventRepository extends JpaRepository<LoanEvent, Long> {
}