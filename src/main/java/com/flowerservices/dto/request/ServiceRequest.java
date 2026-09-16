package com.flowerservices.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ServiceRequest(
        @NotBlank(message = "El nombre del servicio es obligatorio")
        String name,

        String description,

        String iconUrl
) {}