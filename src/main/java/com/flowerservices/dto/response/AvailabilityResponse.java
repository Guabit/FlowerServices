package com.flowerservices.dto.response;

import java.time.DayOfWeek;
import java.time.LocalTime;

public record AvailabilityResponse(
        Long id,
        Long technicianId,
        DayOfWeek dayOfWeek,
        LocalTime startTime,
        LocalTime endTime,
        Integer slotDurationMinutes
) {}