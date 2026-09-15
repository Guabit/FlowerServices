package com.flowerservices.dto.request;

import jakarta.validation.constraints.NotBlank;

public record TechnicianRegistrationRequest(
        @NotBlank(message = "El número de licencia o matrícula es obligatorio")
        String licenseNumber,

        String bio,

        @NotBlank(message = "El área de cobertura es obligatoria")
        String coverageArea
) {}