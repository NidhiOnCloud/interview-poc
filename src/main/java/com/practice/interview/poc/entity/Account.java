package com.practice.interview.poc.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Version;

@Getter
@Setter
@Entity
public class Account {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private int balance;

    @Version
    private Long version;

    public Account() {}

    public Account(String name) {
        this.name = name;
    }

    public Account(int balance){
        this.balance = balance;
    }
}