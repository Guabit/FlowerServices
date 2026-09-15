package com.flowerservices.dto.response;

import com.flowerservices.model.enums.Role;
import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String firstName,
        String lastName,
        String mail,
        String phone,
        Role role,
        Boolean active,
        LocalDateTime createdAt
) {}