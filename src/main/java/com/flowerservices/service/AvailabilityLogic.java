package com.flowerservices.service;

import com.flowerservices.dto.request.AvailabilityRequest;
import com.flowerservices.dto.response.AvailabilityResponse;

import java.util.List;

public interface AvailabilityLogic {
    AvailabilityResponse addAvailability(Long technicianId, AvailabilityRequest request);
    List<AvailabilityResponse> getTechnicianAvailability(Long technicianId);
    void removeAvailability(Long technicianId, Long availabilityId);
}