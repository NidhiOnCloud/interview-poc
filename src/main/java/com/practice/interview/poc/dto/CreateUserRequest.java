package com.practice.interview.poc.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class CreateUserRequest {

    private String userName;

    @Email
    private String email;
}