package com.flowerservices.controller;

import com.flowerservices.dto.request.TechnicianServiceRequest;
import com.flowerservices.dto.response.TechnicianServiceResponse;
import com.flowerservices.model.entity.User;
import com.flowerservices.service.TechnicianServiceLogic; // Tu primer logic de tecnicos para buscar su ID real
import com.flowerservices.service.TechnicianSkillLogic; // La nueva lógica de skills intermedios
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
@RequestMapping("/api/v1/technicians")
@RequiredArgsConstructor
public class TechnicianSkillController {

    private final TechnicianSkillLogic skillLogic;
    private final TechnicianServiceLogic profileLogic; // Para buscar el ID del Técnico partiendo del UserId del login

    /**
     * POST /api/v1/technicians/skills
     * El técnico logueado añade un servicio (Oficio) y fija su tarifa.
     */
    @PostMapping("/skills")
    @PreAuthorize("hasRole('ROLE_TECHNICIAN')")
    public ResponseEntity<TechnicianServiceResponse> addSkill(
            @Valid @RequestBody TechnicianServiceRequest request
    ) {
        Long techId = getLoggedTechnicianId();
        TechnicianServiceResponse response = skillLogic.addSkillToTechnician(techId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * GET /api/v1/technicians/{technicianId}/skills
     * Público: Cualquiera puede ver los oficios que un técnico ofrece y sus precios.
     */
    @GetMapping("/{technicianId}/skills")
    public ResponseEntity<List<TechnicianServiceResponse>> getTechnicianSkills(@PathVariable Long technicianId) {
        return ResponseEntity.ok(skillLogic.getSkillsByTechnician(technicianId));
    }

    /**
     * DELETE /api/v1/technicians/skills/{skillId}
     * El técnico elimina un oficio que ofrecía de su catálogo (ej. "ya no hago electricidad porque subió riesgo")
     */
    @DeleteMapping("/skills/{skillId}")
    @PreAuthorize("hasRole('ROLE_TECHNICIAN')")
    public ResponseEntity<Void> removeSkill(@PathVariable Long skillId) {
        Long techId = getLoggedTechnicianId();
        skillLogic.removeSkill(techId, skillId);
        return ResponseEntity.noContent().build();
    }

    // Helper interno para obtener el ID de Tecnico (Base de datos) del usuario loggeado.
    private Long getLoggedTechnicianId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();

        // Buscamos cuál es el Perfil de 'Técnico' asignado a este Usuario que hizo login
        var profile = profileLogic.getTechnicianByUserId(currentUser.getId());
        return profile.id(); // Retornamos el ID de la tabla "technicians", NO el de "users"
    }
}