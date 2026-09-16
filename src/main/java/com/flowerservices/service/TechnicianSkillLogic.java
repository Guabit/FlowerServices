package com.flowerservices.service;

import com.flowerservices.dto.request.TechnicianServiceRequest;
import com.flowerservices.dto.response.TechnicianServiceResponse;
import java.util.List;

public interface TechnicianSkillLogic {
    TechnicianServiceResponse addSkillToTechnician(Long technicianId, TechnicianServiceRequest request);
    List<TechnicianServiceResponse> getSkillsByTechnician(Long technicianId);
    void removeSkill(Long technicianId, Long skillId);
}