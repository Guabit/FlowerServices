package com.flowerservices.service.impl;

import com.flowerservices.dto.request.TechnicianRegistrationRequest;
import com.flowerservices.dto.response.TechnicianResponse;
import com.flowerservices.exception.ConflictException;
import com.flowerservices.exception.ResourceNotFoundException;
import com.flowerservices.model.entity.Technician;
import com.flowerservices.model.entity.User;
import com.flowerservices.repository.TechnicianRepository;
import com.flowerservices.repository.UserRepository;
import com.flowerservices.service.TechnicianServiceLogic;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TechnicianServiceLogicImpl implements TechnicianServiceLogic {

    private final TechnicianRepository technicianRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public TechnicianResponse registerTechnician(Long userId, TechnicianRegistrationRequest request) {

        // 1. Buscamos al usuario que será técnico
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + userId));

        // 2. Validamos que no tenga un perfil ya creado
        if (technicianRepository.findByUserId(userId).isPresent()) {
            throw new ConflictException("El usuario ya tiene un perfil de técnico registrado.");
        }

        // 3. Creamos la entidad
        Technician technician = Technician.builder()
                .user(user)
                .licenseNumber(request.licenseNumber())
                .bio(request.bio())
                .coverageArea(request.coverageArea())
                // Valores default iniciales
                .averageRating(BigDecimal.ZERO)
                .verified(false)
                .build();

        // 4. Guardamos
        Technician savedTechnician = technicianRepository.save(technician);

        return mapToResponse(savedTechnician);
    }

    @Override
    @Transactional(readOnly = true)
    public TechnicianResponse getTechnicianByUserId(Long userId) {
        Technician technician = technicianRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró perfil de técnico para el usuario ID: " + userId));

        return mapToResponse(technician);
    }

    // Helper map
    private TechnicianResponse mapToResponse(Technician technician) {
        return new TechnicianResponse(
                technician.getId(),
                technician.getUser().getId(),
                technician.getLicenseNumber(),
                technician.getBio(),
                technician.getCoverageArea(),
                technician.getAverageRating(),
                technician.getVerified()
        );
    }
}