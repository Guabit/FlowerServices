package com.flowerservices.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record TechnicianServiceRequest(
        @NotNull(message = "El ID del servicio es obligatorio")
        Long serviceId,

        @NotNull(message = "La tarifa base de visita es obligatoria")
        @Positive(message = "La tarifa debe ser mayor a 0")
        BigDecimal baseVisitFee,

        String feeDescription
) {}