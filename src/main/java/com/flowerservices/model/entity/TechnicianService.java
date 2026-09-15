package com.flowerservices.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "technician_services", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"technician_id", "service_id"})
})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class TechnicianService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "technician_id")
    private Technician technician;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "service_id")
    private Service service;

    @Column(name = "base_visit_fee", nullable = false, precision = 10, scale = 2)
    private BigDecimal baseVisitFee;

    @Column(name = "fee_description")
    private String feeDescription;
}