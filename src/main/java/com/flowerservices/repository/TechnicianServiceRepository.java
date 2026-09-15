package com.flowerservices.repository;

import com.flowerservices.model.entity.TechnicianService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TechnicianServiceRepository extends JpaRepository<TechnicianService, Long> {
    
    List<TechnicianService> findAllByTechnicianId(Long technicianId);
    
    List<TechnicianService> findAllByServiceId(Long serviceId);
}
