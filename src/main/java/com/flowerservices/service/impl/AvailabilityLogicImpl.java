package com.flowerservices.service.impl;

import com.flowerservices.dto.request.AvailabilityRequest;
import com.flowerservices.dto.response.AvailabilityResponse;
import com.flowerservices.exception.ConflictException;
import com.flowerservices.exception.ResourceNotFoundException;
import com.flowerservices.model.entity.Availability;
import com.flowerservices.model.entity.Technician;
import com.flowerservices.repository.AvailabilityRepository;
import com.flowerservices.repository.TechnicianRepository;
import com.flowerservices.service.AvailabilityLogic;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AvailabilityLogicImpl implements AvailabilityLogic {

    private final AvailabilityRepository availabilityRepository;
    private final TechnicianRepository technicianRepository;

    @Override
    @Transactional
    public AvailabilityResponse addAvailability(Long technicianId, AvailabilityRequest request) {

        Technician technician = technicianRepository.findById(technicianId)
                .orElseThrow(() -> new ResourceNotFoundException("Técnico no encontrado"));

        // Validar lógica de negocios: Fin debe ser luego de Inicio
        if (request.endTime().isBefore(request.startTime())) {
            throw new ConflictException("La hora de finalización debe ser posterior a la hora de inicio");
        }

        // Validar si el técnico ya configuró horas de trabajo para ESE MISMO DÍA (Para simplificar, 1 bloque por día)
        boolean hasAvailabilityThatDay = availabilityRepository.findAllByTechnicianId(technicianId).stream()
                .anyMatch(a -> a.getDayOfWeek() == request.dayOfWeek());

        if (hasAvailabilityThatDay) {
            throw new ConflictException("Ya has configurado horarios para el día " + request.dayOfWeek() + ". Si quieres cambiarlo, elimínalo y vuelve a crearlo.");
        }

        Availability availability = Availability.builder()
                .technician(technician)
                .dayOfWeek(request.dayOfWeek())
                .startTime(request.startTime())
                .endTime(request.endTime())
                .slotDurationMinutes(request.slotDurationMinutes())
                .build();

        Availability saved = availabilityRepository.save(availability);
        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AvailabilityResponse> getTechnicianAvailability(Long technicianId) {
        return availabilityRepository.findAllByTechnicianId(technicianId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void removeAvailability(Long technicianId, Long availabilityId) {
        Availability availability = availabilityRepository.findById(availabilityId)
                .orElseThrow(() -> new ResourceNotFoundException("Configuración de horario no encontrada"));

        if (!availability.getTechnician().getId().equals(technicianId)) {
            throw new ConflictException("No puedes eliminar los horarios de otro técnico.");
        }

        availabilityRepository.delete(availability);
    }

    private AvailabilityResponse mapToResponse(Availability a) {
        return new AvailabilityResponse(
                a.getId(),
                a.getTechnician().getId(),
                a.getDayOfWeek(),
                a.getStartTime(),
                a.getEndTime(),
                a.getSlotDurationMinutes()
        );
    }
}