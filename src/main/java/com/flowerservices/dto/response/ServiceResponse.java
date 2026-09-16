package com.flowerservices.dto.response;

public record ServiceResponse(
        Long id,
        String name,
        String description,
        String iconUrl
) {}