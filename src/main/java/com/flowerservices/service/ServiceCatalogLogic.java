package com.flowerservices.service;

import com.flowerservices.dto.request.ServiceRequest;
import com.flowerservices.dto.response.ServiceResponse;
import java.util.List;

public interface ServiceCatalogLogic {
    ServiceResponse createService(ServiceRequest request);
    List<ServiceResponse> getAllServices();
    ServiceResponse getServiceById(Long id);
}