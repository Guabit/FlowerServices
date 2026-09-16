package com.flowerservices.dto.response;

import java.math.BigDecimal;

public record TechnicianServiceResponse(
        Long id,
        Long technicianId,
        ServiceResponse service,
        BigDecimal baseVisitFee,
        String feeDescription
) {}