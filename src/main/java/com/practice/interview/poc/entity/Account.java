package com.practice.interview.poc.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;


@Entity
public class Account {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    public Account() {}

    public Account(String name) {
        this.name = name;
    }
}