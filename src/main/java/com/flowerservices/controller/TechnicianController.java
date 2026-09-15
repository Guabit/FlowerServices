package com.flowerservices.controller;

import com.flowerservices.dto.request.TechnicianRegistrationRequest;
import com.flowerservices.dto.response.TechnicianResponse;
import com.flowerservices.model.entity.User;
import com.flowerservices.service.TechnicianServiceLogic;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/technicians")
@RequiredArgsConstructor
public class TechnicianController {

    private final TechnicianServiceLogic technicianServiceLogic;

    /**
     * POST /api/v1/technicians
     * Permite a un Usuario (que ya hizo login y provee su JWToken) registrarse como Técnico.
     */
    @PostMapping
    @PreAuthorize("hasRole('ROLE_TECHNICIAN')") // Solo usuarios con rol TÉCNICO pueden crearse perfil
    public ResponseEntity<TechnicianResponse> registerTechnician(
            @Valid @RequestBody TechnicianRegistrationRequest request
    ) {
        // Obtenemos al usuario actualmente logueado gracias a nuestro JwtAuthenticationFilter
        Long currentUserId = getCurrentUserId();

        TechnicianResponse response = technicianServiceLogic.registerTechnician(currentUserId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * GET /api/v1/technicians/me
     * Retorna el perfil completo de técnico del usuario logueado actualmente.
     */
    @GetMapping("/me")
    @PreAuthorize("hasRole('ROLE_TECHNICIAN')")
    public ResponseEntity<TechnicianResponse> getMyTechnicianProfile() {
        Long currentUserId = getCurrentUserId();
        TechnicianResponse response = technicianServiceLogic.getTechnicianByUserId(currentUserId);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/v1/technicians/user/{userId}
     * Público, cualquiera puede ver el perfil de un técnico si conoce su Id de usuario.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<TechnicianResponse> getTechnicianProfile(@PathVariable Long userId) {
        TechnicianResponse response = technicianServiceLogic.getTechnicianByUserId(userId);
        return ResponseEntity.ok(response);
    }

    // --- Helper interno para extraer el ID rápido ---
    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();
        return currentUser.getId();
    }
}