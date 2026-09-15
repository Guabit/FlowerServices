package com.flowerservices.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthenticationRequest(
        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "Formato de correo inválido")
        String mail,

        @NotBlank(message = "La contraseña es obligatoria")
        String password
) {}