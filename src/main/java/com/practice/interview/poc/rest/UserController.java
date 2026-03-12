package com.practice.interview.poc.rest;

import com.practice.interview.poc.dto.CreateUserRequest;
import com.practice.interview.poc.entity.User;
import com.practice.interview.poc.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<String> createUser(
            @Valid @RequestBody CreateUserRequest request) {

        userService.createUser(request);
        return ResponseEntity.ok("User created successfully");
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @GetMapping
    public List<User> getUser() {
        return userService.getAllUser();
    }
}