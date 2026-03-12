package com.practice.interview.poc.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;


@Entity
public class OrderEvent {

    @Id
    @GeneratedValue
    private Long id;

    @Getter
    private String message;

    public OrderEvent() {}

    public OrderEvent(String message) {
        this.message = message;
    }
}