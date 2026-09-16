package com.flowerservices.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.DayOfWeek;
import java.time.LocalTime;

public record AvailabilityRequest(
        @NotNull(message = "El día de la semana es obligatorio")
        DayOfWeek dayOfWeek,

        @NotNull(message = "La hora de inicio es obligatoria")
        LocalTime startTime,

        @NotNull(message = "La hora de fin es obligatoria")
        LocalTime endTime,

        @NotNull(message = "La duración de cada turno es obligatoria")
        @Positive(message = "La duración debe ser mayor a 0 minutos")
        Integer slotDurationMinutes
) {}