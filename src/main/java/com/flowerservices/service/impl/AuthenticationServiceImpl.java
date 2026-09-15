package com.flowerservices.service.impl;

import com.flowerservices.config.JwtService;
import com.flowerservices.dto.request.AuthenticationRequest;
import com.flowerservices.dto.response.AuthenticationResponse;
import com.flowerservices.exception.ResourceNotFoundException;
import com.flowerservices.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        // 1. Delegamos a Spring Security la tarea pesada y segura de validar la contraseña encriptada
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.mail(),
                        request.password()
                )
        );

        // 2. Si llega aquí, significa que la contraseña era correcta. Buscamos al usuario en la BD.
        var user = userRepository.findByMail(request.mail())
                .orElseThrow(() -> new ResourceNotFoundException("El correo proporcionado no está registrado"));

        // 3. Le pedimos a nuestro JwtService que construya el Token (firmado criptográficamente).
        var jwtToken = jwtService.generateToken(user);

        // 4. Se lo devolvemos empaquetado
        return new AuthenticationResponse(jwtToken);
    }
}