package com.flowerservices.service.impl;

import com.flowerservices.dto.request.UserRegistrationRequest;
import com.flowerservices.dto.response.UserResponse;
import com.flowerservices.exception.ConflictException;
import com.flowerservices.exception.ResourceNotFoundException;
import com.flowerservices.model.entity.User;
import com.flowerservices.repository.UserRepository;
import com.flowerservices.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // Inyección para encriptar contraseñas

    @Override
    @Transactional
    public UserResponse registerUser(UserRegistrationRequest request) {

        if (userRepository.existsByMail(request.mail())) {
            throw new ConflictException("El correo electrónico ya está registrado en otra cuenta.");
        }

        User user = User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .mail(request.mail())
                .password(passwordEncoder.encode(request.password()))
                .phone(request.phone())
                .role(request.role())
                .active(true)
                .build();

        User savedUser = userRepository.save(user);

        return mapToResponse(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró ningún usuario con el ID: " + id));
        return mapToResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllActiveUsers() {
        return userRepository.findAll().stream()
                .filter(User::getActive)
                .map(this::mapToResponse)
                .toList();
    }

    private UserResponse mapToResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getMail(),
                user.getPhone(),
                user.getRole(),
                user.getActive(),
                user.getCreatedAt()
        );
    }
}