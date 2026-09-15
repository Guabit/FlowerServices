package com.flowerservices.service;

import com.flowerservices.dto.request.TechnicianRegistrationRequest;
import com.flowerservices.dto.response.TechnicianResponse;

public interface TechnicianServiceLogic {
    // Registra a un usuario existente como Técnico
    TechnicianResponse registerTechnician(Long userId, TechnicianRegistrationRequest request);

    // Obtiene el perfil técnico consultándolo por su UserId
    TechnicianResponse getTechnicianByUserId(Long userId);
}