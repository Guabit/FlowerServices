package com.flowerservices.controller;

import com.flowerservices.dto.request.ServiceRequest;
import com.flowerservices.dto.response.ServiceResponse;
import com.flowerservices.service.ServiceCatalogLogic;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/services") // Catálogo de oficios generales
@RequiredArgsConstructor
public class ServiceCatalogController {

    private final ServiceCatalogLogic serviceCatalogLogic;

    /**
     * GET PÚBLICO: Obtiene la lista de todos los oficios que hay en la app (ej. para un Select)
     */
    @GetMapping
    public ResponseEntity<List<ServiceResponse>> getAllServices() {
        return ResponseEntity.ok(serviceCatalogLogic.getAllServices());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceResponse> getServiceById(@PathVariable Long id) {
        return ResponseEntity.ok(serviceCatalogLogic.getServiceById(id));
    }

    /**
     * POST RESTRINGIDO: Solo Admin puede crear nuevos tipos de Oficios en el sistema.
     */
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ServiceResponse> createService(@Valid @RequestBody ServiceRequest request) {
        ServiceResponse response = serviceCatalogLogic.createService(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}