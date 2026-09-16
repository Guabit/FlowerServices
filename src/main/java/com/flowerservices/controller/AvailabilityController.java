package com.flowerservices.controller;

import com.flowerservices.dto.request.AvailabilityRequest;
import com.flowerservices.dto.response.AvailabilityResponse;
import com.flowerservices.model.entity.User;
import com.flowerservices.service.AvailabilityLogic;
import com.flowerservices.service.TechnicianServiceLogic;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/technicians") // Usamos la misma base ruta para agrupar funcionalidades del técnico
@RequiredArgsConstructor
public class AvailabilityController {

    private final AvailabilityLogic availabilityLogic;
    private final TechnicianServiceLogic profileLogic;

    /**
     * POST /api/v1/technicians/availability
     * El Técnico establece en qué marco horario trabaja los Lunes, Martes, etc.
     */
    @PostMapping("/availability")
    @PreAuthorize("hasRole('ROLE_TECHNICIAN')")
    public ResponseEntity<AvailabilityResponse> addAvailability(
            @Valid @RequestBody AvailabilityRequest request
    ) {
        Long techId = getLoggedTechnicianId();
        AvailabilityResponse response = availabilityLogic.addAvailability(techId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * GET /api/v1/technicians/{technicianId}/availability
     * Público: Permite a la web de Next.js dibujar el calendario disponible de un técnico.
     */
    @GetMapping("/{technicianId}/availability")
    public ResponseEntity<List<AvailabilityResponse>> getTechnicianAvailability(@PathVariable Long technicianId) {
        return ResponseEntity.ok(availabilityLogic.getTechnicianAvailability(technicianId));
    }

    /**
     * DELETE /api/v1/technicians/availability/{availabilityId}
     * El Técnico borra los horarios de un día.
     */
    @DeleteMapping("/availability/{availabilityId}")
    @PreAuthorize("hasRole('ROLE_TECHNICIAN')")
    public ResponseEntity<Void> removeAvailability(@PathVariable Long availabilityId) {
        Long techId = getLoggedTechnicianId();
        availabilityLogic.removeAvailability(techId, availabilityId);
        return ResponseEntity.noContent().build();
    }

    // Helper (Igual al de las habilidades, centralizable después)
    private Long getLoggedTechnicianId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();
        var profile = profileLogic.getTechnicianByUserId(currentUser.getId());
        return profile.id();
    }
}