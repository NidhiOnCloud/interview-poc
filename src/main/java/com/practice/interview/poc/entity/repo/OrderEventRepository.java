package com.practice.interview.poc.entity.repo;

import com.practice.interview.poc.entity.OrderEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderEventRepository extends JpaRepository<OrderEvent, Long> {
}