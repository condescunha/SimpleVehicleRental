package tech.clavem303.DTOs;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record BookingCreateDTO(
        @NotNull
        Long vehicleId,
        @NotNull
        LocalDate startDate,
        @NotNull
        LocalDate endDate
) {
}
