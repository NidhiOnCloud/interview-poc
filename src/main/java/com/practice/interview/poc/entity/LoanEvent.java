package com.practice.interview.poc.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class LoanEvent {

    @Id
    @GeneratedValue
    private Long id;

    private String message;

    public LoanEvent() {}

    public LoanEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}