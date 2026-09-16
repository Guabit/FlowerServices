package com.flowerservices.service.impl;

import com.flowerservices.dto.request.ServiceRequest;
import com.flowerservices.dto.response.ServiceResponse;
import com.flowerservices.exception.ResourceNotFoundException;
import com.flowerservices.model.entity.Service;
import com.flowerservices.repository.ServiceRepository;
import com.flowerservices.service.ServiceCatalogLogic;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceCatalogLogicImpl implements ServiceCatalogLogic {

    private final ServiceRepository serviceRepository;

    @Override
    @Transactional
    public ServiceResponse createService(ServiceRequest request) {
        Service service = Service.builder()
                .name(request.name())
                .description(request.description())
                .iconUrl(request.iconUrl())
                .build();

        Service savedService = serviceRepository.save(service);
        return mapToResponse(savedService);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServiceResponse> getAllServices() {
        return serviceRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ServiceResponse getServiceById(Long id) {
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Oficio no encontrado con el ID: " + id));
        return mapToResponse(service);
    }

    private ServiceResponse mapToResponse(Service service) {
        return new ServiceResponse(
                service.getId(),
                service.getName(),
                service.getDescription(),
                service.getIconUrl()
        );
    }
}