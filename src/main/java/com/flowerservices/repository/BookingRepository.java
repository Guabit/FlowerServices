package com.flowerservices.repository;

import com.flowerservices.model.entity.Booking;
import com.flowerservices.model.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByCustomerId(Long customerId);
    
    List<Booking> findAllByTechnicianId(Long technicianId);

    // Evitar Double-Booking (Overlapping time slots validation)
    @Query("SELECT COUNT(b) > 0 FROM Booking b " +
           "WHERE b.technician.id = :technicianId " +
           "AND b.bookingDate = :bookingDate " +
           "AND b.status NOT IN ('REJECTED', 'CANCELLED') " +
           "AND ((b.startTime < :endTime AND b.endTime > :startTime))")
    boolean existsOverlappingBooking(
        @Param("technicianId") Long technicianId,
        @Param("bookingDate") LocalDate bookingDate,
        @Param("startTime") LocalTime startTime,
        @Param("endTime") LocalTime endTime
    );
}
