package com.flowerservices.service.impl;

import com.flowerservices.dto.request.TechnicianServiceRequest;
import com.flowerservices.dto.response.ServiceResponse;
import com.flowerservices.dto.response.TechnicianServiceResponse;
import com.flowerservices.exception.ConflictException;
import com.flowerservices.exception.ResourceNotFoundException;
import com.flowerservices.model.entity.Service;
import com.flowerservices.model.entity.Technician;
import com.flowerservices.model.entity.TechnicianService;
import com.flowerservices.repository.ServiceRepository;
import com.flowerservices.repository.TechnicianRepository;
import com.flowerservices.repository.TechnicianServiceRepository;
import com.flowerservices.service.TechnicianSkillLogic;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class TechnicianSkillLogicImpl implements TechnicianSkillLogic {

    private final TechnicianServiceRepository technicianServiceRepository;
    private final TechnicianRepository technicianRepository;
    private final ServiceRepository catalogRepository;

    @Override
    @Transactional
    public TechnicianServiceResponse addSkillToTechnician(Long technicianId, TechnicianServiceRequest request) {

        // 1. Validar que el técnico existe
        Technician technician = technicianRepository.findById(technicianId)
                .orElseThrow(() -> new ResourceNotFoundException("Técnico no encontrado."));

        // 2. Validar que el Servicio (Oficio) existe en el catálogo
        Service catalogService = catalogRepository.findById(request.serviceId())
                .orElseThrow(() -> new ResourceNotFoundException("Oficio no encontrado en el catálogo."));

        // 3. (REGLA NEGOCIO) Evitar duplicados: Un técnico no puede ofrecer 'Plomería' 2 veces.
        boolean alreadyOffers = technicianServiceRepository.findAllByTechnicianId(technicianId).stream()
                .anyMatch(ts -> ts.getService().getId().equals(request.serviceId()));

        if (alreadyOffers) {
            throw new ConflictException("Ya estás ofreciendo este servicio. Solo puedes actualizar tus tarifas.");
        }

        // 4. Crear asociación
        TechnicianService newSkill = TechnicianService.builder()
                .technician(technician)
                .service(catalogService)
                .baseVisitFee(request.baseVisitFee())
                .feeDescription(request.feeDescription())
                .build();

        TechnicianService savedSkill = technicianServiceRepository.save(newSkill);

        return mapToResponse(savedSkill);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TechnicianServiceResponse> getSkillsByTechnician(Long technicianId) {
        return technicianServiceRepository.findAllByTechnicianId(technicianId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void removeSkill(Long technicianId, Long skillId) {
        TechnicianService skill = technicianServiceRepository.findById(skillId)
                .orElseThrow(() -> new ResourceNotFoundException("Asignación de oficio no encontrada"));

        // Regla de seguridad: Solo puede borrarlo si es SU asignación
        if (!skill.getTechnician().getId().equals(technicianId)) {
            throw new ConflictException("No tienes permiso para eliminar esta habilidad.");
        }

        technicianServiceRepository.delete(skill);
    }

    // Helper interno de Mapeo
    private TechnicianServiceResponse mapToResponse(TechnicianService ts) {
        ServiceResponse sInfo = new ServiceResponse(
                ts.getService().getId(), ts.getService().getName(), ts.getService().getDescription(), ts.getService().getIconUrl()
        );

        return new TechnicianServiceResponse(
                ts.getId(),
                ts.getTechnician().getId(),
                sInfo,
                ts.getBaseVisitFee(),
                ts.getFeeDescription()
        );
    }
}