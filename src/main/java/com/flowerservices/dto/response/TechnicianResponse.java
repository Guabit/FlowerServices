package com.flowerservices.dto.response;

import java.math.BigDecimal;

public record TechnicianResponse(
        Long id,
        Long userId,
        String licenseNumber,
        String bio,
        String coverageArea,
        BigDecimal averageRating,
        Boolean verified
) {}