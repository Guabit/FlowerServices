package com.flowerservices.controller;

import com.flowerservices.dto.request.UserRegistrationRequest;
import com.flowerservices.dto.response.UserResponse;
import com.flowerservices.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // POST http://localhost:8081/api/v1/users
    // El @Valid dispara las validaciones que pusimos en el Record.
    @PostMapping
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserRegistrationRequest request) {
        UserResponse response = userService.registerUser(request);
        // Devuelve código HTTP 201 (Created)
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // GET http://localhost:8081/api/v1/users/1
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        UserResponse response = userService.getUserById(id);
        return ResponseEntity.ok(response);
    }

    // GET http://localhost:8081/api/v1/users
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllActiveUsers() {
        List<UserResponse> responses = userService.getAllActiveUsers();
        return ResponseEntity.ok(responses);
    }
}