package com.practice.interview.poc.service;

import com.practice.interview.poc.dto.CreateUserRequest;
import com.practice.interview.poc.entity.User;
import com.practice.interview.poc.entity.repo.UserRepository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void createUser(CreateUserRequest request) {

        if (userRepository.existsByUsername(request.getUserName())) {
            throw new IllegalArgumentException("User already exists");
        }

        User user = new User();
        user.setUsername(request.getUserName());
        user.setEmail(request.getEmail());

        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "users", key = "#id")
    public User getUser(Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    @Transactional(readOnly = true)
    public List<User> getAllUser() {
        return userRepository.findAll();
    }
}