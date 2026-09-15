package com.flowerservices.service;

import com.flowerservices.dto.request.UserRegistrationRequest;
import com.flowerservices.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse registerUser(UserRegistrationRequest request);
    UserResponse getUserById(Long id);
    List<UserResponse> getAllActiveUsers();
}